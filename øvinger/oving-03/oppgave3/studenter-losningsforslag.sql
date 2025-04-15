SET search_path = forelesning3;

-- 3b : Lag en spørring som henter ut alle studenter med student_id, fornavn, etternavn og studieprogram.
SELECT
   s.student_id,
   student_data->>'fornavn'			fornavn,
   student_data->>'etternavn' 		etternavn,
   student_data->>'studieprogram' 	studieprogram
FROM studenter s
ORDER BY s.student_id asc;

SELECT
   s.student_id,
   jsonb_path_query(student_data,'$.fornavn')->>0		  fornavn,
   jsonb_path_query(student_data,'$.etternavn')->>0 	  etternavn,
   jsonb_path_query(student_data,'$.studieprogram')->>0 studieprogram
FROM studenter s
ORDER BY s.student_id asc;

-- 3c : Endre spørringen fra oppgaven over slik at fornavn og etternavn slåes sammen (konkateneres med blankt tegn imellom) i en egen kolonne med alias «navn».
SELECT
   s.student_id,
   (student_data->>'fornavn') || ' ' || (student_data->>'etternavn') navn,
   student_data->>'studieprogram' studieprogram
FROM studenter s
ORDER BY s.student_id asc;

SELECT
   s.student_id,
   (jsonb_path_query(student_data,'$.fornavn')->>0) || ' ' ||
   (jsonb_path_query(student_data,'$.etternavn')->>0) 	  	navn,
   jsonb_path_query(student_data,'$.studieprogram')->>0 		studieprogram
FROM studenter s
ORDER BY s.student_id asc;

-- 3d : Samme spørring som over, men legg til en kolonne «studiestart».
--      Studiestart skal være en sammenslåing av semester og årstall, f.eks. ‘H2024’.

SELECT
   s.student_id,
   (student_data->>'fornavn') || ' ' || (student_data->>'etternavn') navn,
   student_data->>'studieprogram' studieprogram,
   (student_data->'studiestart'->>'semester') || (student_data->'studiestart'->>'år') studiestart
FROM studenter s
ORDER BY s.student_id asc;

SELECT
   s.student_id,
   (jsonb_path_query(student_data,'$.fornavn')->>0) || ' ' ||
   (jsonb_path_query(student_data,'$.etternavn')->>0) 	  	navn,
   jsonb_path_query(student_data,'$.studieprogram')->>0 		studieprogram,
   (jsonb_path_query(student_data,'$.studiestart.semester')->>0) ||
   (jsonb_path_query(student_data,'$.studiestart.år')->>0) 	studiestart
FROM studenter s
ORDER BY s.student_id asc;

-- 3e, 3f: Sammenlign spørringene under
--         Den første har datatypen JSONB, mens den siste returnerer datatypen "text"
SELECT jsonb_path_query('{"values":[1, 2, 3, 4, 5]}', '$.values[*] ? (@ > 3)');
SELECT jsonb_path_query('{"values":[1, 2, 3, 4, 5]}', '$.values[*] ? (@ > 3)')::text;

-- 3g : Lag en spørring som henter ut alle studenter som har tatt emnet «DAT152». 
--      Spørringen skal hente ut samme data som i oppgave d), 
--      dvs. student_id, fornavn, etternavn, studiestart og studieprogram.

SELECT
   student_id,
   student_data->>'fornavn' fornavn,
   student_data->>'etternavn' etternavn,
   student_data->>'studieprogram' studieprogram,
   (student_data->'studiestart'->>'semester') || (student_data->'studiestart'->>'år') studiestart
FROM studenter
WHERE jsonb_path_exists(student_data, '$.emner[*] ? (@.kode == "DAT152")');

-- 3h : Lag en UPDATE spørring som bruker JSONB_SET til å oppdatere fornavnet til student_id = 101 til «Olav»
update studenter s
set student_data = jsonb_set(student_data, '{fornavn}','"Olav"')
where student_id = 101
   RETURNING *;

-- 3i : Lag en UPDATE spørring som bruker JSONB_SET til å oppdatere poengene som student_id=101 
--      har fått i emnet DAT100 til 99.
select student_id, elem, position - 1 pos
from studenter, jsonb_array_elements(student_data->'emner') with ordinality arr(elem, position)
where elem->>'kode' = 'DAT100' and studenter.student_id=101;

update studenter s
set student_data = jsonb_set(student_data, '{emner, 0, poeng}','99')
where student_id = 101
   RETURNING *;

-- 3j : Lag en spørring som henter ut alle fag som student_id=101 har tatt. I tillegg til skrive
--      ut emne «kode» og «poeng» som egne kolonner (se under), skal også det aktuelle
--      emnets posisjon i array «emner» være med som egen kolonne (se under).

SELECT
   student_id,
   jsonb_path_query("student_data",'$.fornavn')->>0                     fornavn,
   obj->>'kode' AS kode,
   obj->>'poeng' AS poeng,
   --emne,
   --obj,
   pos-1 pos
FROM studenter,
   jsonb_array_elements(student_data->'emner') WITH ORDINALITY AS emne(obj, pos)
WHERE student_id=101;


-- All poeng oppnådd i DAT100 emnet
select
   s.student_id
     ,jsonb_path_query(student_data,'$.emner[*] ? (@.kode == "DAT100")') -> 'poeng' dat100poeng
from studenter s;

select student_data, jsonb_pretty(student_data) from studenter where student_id=101;

-- JOIN med nøstet array
SELECT
   student_id,
   jsonb_path_query("student_data",'$.fornavn')->>0                     fornavn,
   obj->>'kode' AS kode,
   obj->>'poeng' AS poeng,
   --emne,
   --obj,
   pos-1 pos
FROM studenter,
   jsonb_array_elements(student_data->'emner') WITH ORDINALITY AS emne(obj, pos)
WHERE student_id=101;