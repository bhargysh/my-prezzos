# Explaining Postgres DB

![](https://media.giphy.com/media/26gsccje7r5WUrXsA/giphy.gif)

---

## Agenda

- 🧠 What tools you can use to analyse database queries
- 🙅‍♀️ How to optimise a database

---

## Use of postgres

![search result for postgres in github](postgres-github-search.png)

![schitts creek shook](https://media1.giphy.com/media/l41YpJAQAI5fQTNyo/giphy.gif)

---

## Learning goals

- Better understanding of how databases do (some) of their magic 🔮
- Know which tools can help debug database performance issues
- Bonus: refresher on some fun CS concepts

---

## Some DB requirements

- SLAs on how performant a database should be
- Want to read from memory than from disk
    - _Caveat_: large datasets can't all be in memory

---

## Inside postgres

- Postgres is implemented using a B-tree
- self-balancing binary search tree
- allows searches, sequential operations in `log` time

---

## Small CS digression

Btree
- data structure that sorts itself
- each node can have more than two children
- ✅ helps reduce access to disk
    - e.g. when a column is indexed in your search, postgres uses the index to access data on disk quicker

---

![](https://www.codeproject.com/KB/recipes/1158559/btree-figure1.png)

---

## Thoughts, questions, comments?

![](https://media.giphy.com/media/26gZ0LDcaRq9p7mqA/giphy.gif)

---

## A query

```sql
(SELECT id, data::text, modified_at
FROM "listings"
WHERE ("modified_at" > '2020-01-01T00:00:00+00:00')
ORDER BY "modified_at", "id" LIMIT 1000)
UNION
SELECT id, data::text, modified_at
FROM "listings"
WHERE (("modified_at" = '2020-01-01T00:00:00+00:00') AND ("id" > 1111111))
ORDER BY "modified_at", "id" LIMIT 1000;
```
---

## What's happening under the hood? 👀
Hold onto that...for a few mins

---

## Tool to use 🛠

- _Query planner_ calculates the cumulative costs for each different execution strategy and chooses the most optimal plan (not always the lowest 'cost')

---

| Command | What it does |
| --- | --- |
|`EXPLAIN`| - how the query planner _plans_ to execute the given query<br>- estimates time taken |
|`EXPLAIN ANALYZE`| - query planner _executes_ the query<br> -  returns time spent on executing each part of the query |

---

## Analyse our query

```sql
explain analyze (SELECT id, data::text, modified_at
FROM "listings"
WHERE ("modified_at" > '2020-01-01T00:00:00+00:00')
ORDER BY "modified_at", "id" LIMIT 1000)
UNION
SELECT id, data::text, modified_at
FROM "listings"
WHERE (("modified_at" = '2020-01-01T00:00:00+00:00') AND ("id" > 1111111))
ORDER BY "modified_at", "id" LIMIT 1000;
```

---

## Output

```
Limit  (cost=297.32..297.82 rows=200 width=44) (actual time=15.796..15.821 rows=200 loops=1)
   ->  Sort  (cost=297.32..297.98 rows=264 width=44) (actual time=15.795..15.806 rows=200 loops=1)
         Sort Key: listings.modified_at, listings.id
         Sort Method: quicksort  Memory: 1783kB
         ->  HashAggregate  (cost=284.07..286.71 rows=264 width=44) (actual time=14.763..14.815 rows=200 loops=1)
               Group Key: listings.id, ((listings.data)::text), listings.modified_at
               ->  Append  (cost=0.56..282.09 rows=264 width=44) (actual time=0.088..13.638 rows=200 loops=1)
                     ->  Limit  (cost=0.56..109.61 rows=200 width=44) (actual time=0.088..13.604 rows=200 loops=1)
                           ->  Index Scan using idx_listings_modified_at_id on listings  (cost=0.56..942853.55 rows=1729285 width=44) (actual time=0.087..13.581 rows=200 loops=1)
                                 Index Cond: (modified_at > '2020-01-01 00:00:00+00'::timestamp with time zone)
                     ->  Index Scan using idx_listings_modified_at_id on listings listings_1  (cost=0.56..168.52 rows=64 width=44) (actual time=0.006..0.006 rows=0 loops=1)
                           Index Cond: ((modified_at = '2020-01-01 00:00:00+00'::timestamp with time zone) AND (id > 1111111))
 Planning Time: 0.174 ms
 Execution Time: 15.876 ms
```
---
## Cost of query

```
(cost=0.56..168.52 rows=64 width=44)
```

- Cost:
    - Postgres expects it will cost `168.52 units` to find the values
    - `0.56` is the cost for the node to begin working (startup time)
    - `rows` => the estimated no. of rows returned from query
    - `width` => estimated size in bytes of returned rows

---

## Actual query

```
(actual time=0.006..0.006 rows=0 loops=1)
```

- Actual
    - Index scan was executed 1 time (`loops`)
    - Returned 0 rows
    - `time` average of an iteration, if the index scan was executed >1 => `loops` * `time`

---

## Deciphering the rest

1. Any line (other than first) that does _not_ start with `->` provides additional executions stats.
2. Any line that starts with `->` provides _additional_ execution stats and structure of the query.

```
Limit
-----> Sort
-----------> HashAggregate
--------------------------> Append
----------------------------------> Limit
-----------------------------------------> Index Scan
----------------------------------> Index Scan

```

---

## Digging deeper

- Work inside-out to understand what happens _"first"_

```sql
Index Scan using idx_listings_modified_at_id on listings -- 1st query
Index Scan using idx_listings_modified_at_id on listings listings_1 -- 2nd query
-- and so on...
```

---
## Bubble up 🧼

- Result of the first index scan is passed up to `Append`
    - Append is used when rows need to be combined into a single result
    - The query has `UNION` in it, which is why `Append` is used

---

## Bubble up x2

- Result of the append is then passed to `HashAggregate`, which:
  1. Builds a hash table
  2. Groups any (unsorted) data

---

## Bubble up x3
- `Sort` includes info about the algorithm and whether it was done in memory or on disk and amount of memory needed

```sql
Sort Method: quicksort  Memory: 1783kB
```
---

## Thoughts, questions, comments?

![](https://media.giphy.com/media/l0JMeWwmFD2tHLQaI/giphy.gif)

---

## A gotcha 🐵

- _Same query_ can use an index during one run and use a sequential scan with a **filter** instead on a different run
- WHY? 🤔

---

## Number of rows

```
Limit  (cost=297.32..297.82 rows=200 width=44) (actual time=15.796..15.821 rows=200 loops=1)
```
[Real example](https://git.realestate.com.au/listings/listing-publisher-api/issues/539#issuecomment-252771)

---

## Thoughts, questions, comments?

![schitts creek](https://media1.giphy.com/media/l3V0xeOhH2AjrSUak/giphy.gif)

---

![schitts creek](https://media0.giphy.com/media/fVtcfEXWQJQUbsF1sH/giphy.gif)
