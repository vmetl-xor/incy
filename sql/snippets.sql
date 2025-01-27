select count(*) as word_stats from site_statistics;
select count(*) as sites from sites;
select count(*) as words from words;

select s.name, w.value, ss.word_occurrences
from sites s
         inner join public.site_statistics ss on s.id = ss.site_id
         inner join public.words w on ss.word_id = w.id
where s.name like '%wikipedia%'
order by ss.word_occurrences desc;

select s.name, w.value, ss.word_occurrences
from sites s
         inner join public.site_statistics ss on s.id = ss.site_id
         inner join public.words w on ss.word_id = w.id
-- where s.name = 'https://meta.wikimedia.org'
order by ss.word_occurrences desc
;

select s.name, w.value, ss.word_occurrences
from sites s
         inner join public.site_statistics ss on s.id = ss.site_id
         inner join public.words w on ss.word_id = w.id
where w.value = 'राजकारणी'
order by ss.word_occurrences desc
;

select w.value, sum(ss.word_occurrences)
from sites s
         inner join public.site_statistics ss on s.id = ss.site_id
         inner join public.words w on ss.word_id = w.id
group by w.value
order by sum(ss.word_occurrences) desc
;



select id, *
from sites
where name not like '%https%';

select * from words where value = 'राजकारणी'

