SET search_path = forelesning2;

select * from studenter;

-- ----------------------------------------------------------------------------------
-- 3a : List ut «student_no» og «navn» på alle studenter.
--      NB! Kolonnen «navn» skal være fornavn og etternavn med blankt tegn imellom.
-- ----------------------------------------------------------------------------------
-- Først via:
SELECT student_no,
       XPath('/student/fornavn/text()', xml_info) fornavn,
       XPath('/student/etternavn/text()', xml_info) etternavn
FROM studenter;
--WHERE xpath_exists('/student[starts-with(fornavn,"N")]', xml_info);

-- Så løsning:
SELECT student_no,
       unnest(XPath('/student/fornavn/text()', xml_info)) || ' ' ||
       unnest(XPath('/student/etternavn/text()', xml_info)) navn
FROM studenter;

-- ----------------------------------------------------------------------------------
-- 3b : Finn fornavn og etternavn på alle studenter som har fornavn som starter på «N».
--      Hint! Xpath_exist og funksjonen «starts-with(<data>,<søkeverdi>)».
-- ----------------------------------------------------------------------------------
SELECT student_no,
       XPath('/student/fornavn/text()', xml_info) fornavn,
       XPath('/student/etternavn/text()', xml_info) etternavn
FROM studenter
   WHERE xpath_exists('/student[starts-with(fornavn,"N")]', xml_info);

-- ----------------------------------------------------------------------------------
-- 3c : List ut alle fag som de ulike studentene har tatt (studentene skal være representert med «student_no»).
-- ----------------------------------------------------------------------------------
select
   student_no,
   (XPath('/student/emner/emne/kode/text()', xml_info)) fag_array,
   (XPath('/student/emner/emne/kode/text()', xml_info))::text fag_text
from studenter;

-- ----------------------------------------------------------------------------------
-- 3d : List ut alle fag som en student har gjennomført som egne rader - sammen med
--      poeng summen oppnådd i hvert fag.
-- ----------------------------------------------------------------------------------
select s.student_no, x.kode, x.poeng
from studenter s
        cross join xmltable('/student/emner/emne' passing s.xml_info
                                columns kode text path 'kode',
                            poeng integer path 'poeng') as x;

-- Legger til fornavn og etternavn (dobbel CROSS JOIN)
select s.student_no, n.fornavn, n.etternavn, x.kode, x.poeng
from studenter s cross join xmltable('/student' passing s.xml_info
                                columns fornavn text path 'fornavn',
                                     etternavn text path 'etternavn') as n
                 cross join xmltable('/student/emner/emne' passing s.xml_info
                                columns kode text path 'kode',
                                     poeng integer path 'poeng') as x;