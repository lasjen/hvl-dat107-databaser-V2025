-- Generate JSON for kategori og vare embedded
-- This script generates JSON data for kategori
-- Note! Her har jeg ikke tatt hensyn til datatyper (oppgave for dere)
set search_path=oblig4;

select * from kategori;
select * from vare;

-------------------------------------
-- Lager indre spørring først
-------------------------------------
select
   v.katnr,
   json_agg(
         json_build_object('vnr', v.vnr,   'betegnelse', v.betegnelse,
                           'pris', v.pris, 'hylle', v.hylle)
   ) varer
from vare v
group by v.katnr
order by 1;

-------------------------------------
-- JSON from KATEGORI
-------------------------------------

SELECT
   json_build_object('katnr', k.katnr, 'navn', k.navn, 'varer', o.varer)
FROM kategori k inner join (
   select
      v.katnr,
      json_agg(
            json_build_object('vnr', v.vnr,
                              'betegnelse', v.betegnelse,
                              'pris', v.pris,
                              'hylle', v.hylle)
      ) as varer
   from vare v
   group by v.katnr) o
                           on (k.katnr=o.katnr);