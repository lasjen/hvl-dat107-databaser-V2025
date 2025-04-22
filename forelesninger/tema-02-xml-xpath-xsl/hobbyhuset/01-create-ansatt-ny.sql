set search_path=oblig4;

select * from ansatt;

drop table if exists ansatt_ny;

create table ansatt_ny (
                          ansatt_id integer constraint ansatt_ny_pk primary key,
                          ansatt_xml xml    constraint ansatt_xml_nn not null
);
commit;

select * from ansatt_ny;


--INSERT INTO ansatt_ny (ansatt_id, ansatt_xml)
SELECT
   ansnr,
   xmlelement(name "ansatt",
              xmlforest())
FROM ansatt as a inner join poststed as p on (a.postnr=p.postnr);

-- Example: xpath, xpath_exists, unnest, cast, ::integer, ::numeric (xml -> text -> tall)
--          starts-with, ends-with, contains
SELECT
   ansatt_id,
   cast(
         unnest(xpath('/ansatt/navn/text()', ansatt_xml))
      as text) navn,
   cast(unnest(xpath('/ansatt/aarslonn/text()', ansatt_xml)) as text)::numeric lonn
FROM ansatt_ny
WHERE 1=1
  AND xpath_exists('/ansatt[kjonn="M"]', ansatt_xml)
  --AND xpath_exists('/ansatt[contains(navn,"Georg")]', ansatt_xml)
;









// Fasit INSERT
INSERT INTO ansatt_ny (ansatt_id, ansatt_xml)
SELECT
   ansnr,
   xmlelement(name "ansatt",
              xmlforest(a.fornavn || ' ' || a.etternavn as navn,
                        a.adresse || ', ' || a.postnr || ' '||p.poststed as adresse,
                        a.fØdselsdato as fdato,
                        a.kjØnn as kjonn,
                        a.stilling,
                        a.ÅrslØnn as aarslonn))
FROM ansatt as a inner join poststed as p on (a.postnr=p.postnr);
commit;