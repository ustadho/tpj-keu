
--14 Mei 2008
create or replace function fn_nota_get_no_bayar(varchar)
returns varchar
as
$$
declare 
	v_tgl_bayar alias for $1;
	v_new_kode varchar;
begin
	select into v_new_kode to_Char(v_tgl_bayar::date, 'yyyyMMdd')|| trim(to_char((substring(no_bayar from '...$'):: int )+1, '0000'))
	from nota_pembayaran
	where tanggal= v_tgl_bayar::date  order by no_bayar desc;

v_new_kode:=coalesce(v_new_kode, to_Char(v_tgl_bayar::date, 'yyyyMMdd')||'0001');

return v_new_kode;
-- select fn_nota_get_no_bayar('2008-09-01')
end
$$
language 'plpgsql'

--===========================================================================================
CREATE OR REPLACE FUNCTION tg_keu_cek_item_nota()
  RETURNS "trigger" AS
$BODY$

BEGIN	
	IF (TG_OP = 'INSERT' or TG_OP='UPDATE') THEN
		if not exists(select * from nota_item_trx where item_trx=new.item_trx) then
			insert into nota_item_trx(item_trx) values(new.item_trx);
		
		end if;
            ---RETURN NEW;
	END IF;
	

        RETURN NULL; -- result is ignored since this is an AFTER trigger	
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER tg_keu_cek_item_nota
  AFTER INSERT OR UPDATE
  ON nota_detail
  FOR EACH ROW
  EXECUTE PROCEDURE tg_keu_cek_item_nota();

--===========================================================================================

select d.no_nota, --case when toko.nama is null then coalesce(c.nama,'') else coalesce(toko.nama, '') end as nama_toko,
sum(coalesce(sub_total, 0)) as grand_total, 0 as terbayar
from nota_detail d, nota n ,customer c , toko
where n.no_nota=d.no_nota
and (case when tagihan_per='T' then c.kode_cust=customer else toko.kode_toko=customer end)
and seri_kapal=4
group by kondisi,d.no_nota, case when tagihan_per='T' then c.nama else toko.nama end
order by d.no_nota

select kondisi, a.no_nota, tgl_nota, case when tagihan_per='T' then c.nama else toko.nama end,
sum(grand_total) as total_nota, sum(jml_bayar) as jml_bayar, sum(jml_klem) as jml_klem
from(
	select d.no_nota, --case when toko.nama is null then coalesce(c.nama,'') else coalesce(toko.nama, '') end as nama_toko,
	sum(coalesce(sub_total, 0)) as grand_total, 0 as jml_bayar, 0 as jml_klem
	from nota_detail d inner join nota n on n.no_nota=d.no_nota
	where seri_kapal=4
	group by d.no_nota

	union all
	select d.no_nota, 0 as total_nota, sum(coalesce(jumlah,0)) as jml_bayar, sum(coalesce(klem,0)) as jml_klem
	from nota_pembayaran_detail d, nota_pembayaran p
	where p.no_bayar=d.no_bayar 
	group by d.no_nota
)a, nota, toko, customer c
where nota.no_nota=a.no_nota
and (case when tagihan_per='T' then c.kode_cust=customer else toko.kode_toko=customer end)
group by kondisi, a.no_nota, tgl_nota ,case when tagihan_per='T' then c.nama else toko.nama end

--case when tagihan_per='T' then c.nama else toko.nama end,
