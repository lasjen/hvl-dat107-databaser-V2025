-- Generate JSON for PRISHISTORIKK (SIMPLE JSON without EMBEDDING ARRAYS)
-- This script generates JSON data for prishistorikk based on the provided SQL query.
set search_path=oblig4;

select * from prishistorikk;

-------------------------------------
-- JSON from PRISHISTORIKK
-------------------------------------

-- Simple format
SELECT
   (json_build_object('vnr', (ph.vnr)::integer,
                      'dato', ph.dato,
                      'gammelpris', (ph.gammelpris)::text
    )
      )::text
FROM prishistorikk ph  LIMIT 100;

-- More complex with BSON datatypes
SELECT
   (json_build_object('vnr', ph.vnr::integer,
                      'dato', json_build_object('$date',ph.dato),
                      'gammelpris', json_build_object('$numberDecimal',(ph.gammelpris)::text)
    )
      )::text
FROM prishistorikk ph LIMIT 100;