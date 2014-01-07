-- Function: fn_rpt_penerimaan_per_tanggal_kota_v1(character varying, character varying, character varying)

-- DROP FUNCTION fn_rpt_penerimaan_per_tanggal_kota_v1(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION fn_rpt_penerimaan_per_tanggal_kota_toko_tujuan(character varying, character varying, character varying, varchar, varchar)
  RETURNS SETOF record AS
$BODY$
declare 
	v_tgl1 alias for $1;
	v_tgl2 alias for $2;
	v_kota alias for $3;
	v_dari alias for $4;
	v_tujuan alias for $5;
	r record;

begin
for r in
	select 
	pnd.no_penerimaan, to_char(pn.tanggal,'dd-MM-yyyy') as tgl_masuk, coalesce(toko.nama,'') as toko,
	--coalesce(c.nama,'') as nama_toko_tujuan,
	pn.merk ||' ('||coalesce(c.nama,'')||') ' as merk_toko,
	coalesce(ex.no_spnu,'') as container, 
	--coalesce(pnd.qty,0) as qty,
	ex.coli , ex.kode_barang , nama_barang , 
	case when pnd.is_paket then 'Paket' else '' end as ket_paket,
	coalesce(ex.panjang,0) as panjang, coalesce(ex.lebar,0) as lebar, coalesce(ex.tinggi,0) as tingi,
	case when pnd.is_paket then 'Paket' else 
		case 	when pnd.lebar =0 and pnd.tinggi=0 and pnd.panjang=0 then ''
			when pnd.lebar =0 and pnd.tinggi=0 and pnd.panjang<>0 then pnd.panjang::text
			when pnd.lebar<>0 and pnd.tinggi=0 and pnd.panjang<>0  then pnd.panjang::varchar ||' x '|| pnd.lebar::varchar
			when pnd.lebar >0 and pnd.tinggi>0 and pnd.panjang>0 then pnd.panjang::varchar ||' x '|| pnd.lebar::varchar ||' x ' ||pnd.tinggi::varchar
		end
	end as ukuran,
	case when pnd.is_fix_vol is true then coalesce(pnd.fix_vol,0) else fn_get_volume(coalesce(ex.panjang,0), coalesce(ex.lebar,0), coalesce(ex.tinggi,0), coalesce(ex.coli,0)) end as vol, 
	coalesce(pnd.keterangan,'') as keterangan, coalesce(pn.user_ins,'') as user_ins
	from penerimaan pn 
	inner join penerimaan_detail pnd on pnd.no_penerimaan=pn.no_penerimaan 
	left join expedisi_barang ex on ex.serial=pnd.id
	left join barang b on b.kode_barang=ex.kode_barang
	left join kontainer kn on kn.no_spnu=ex.no_spnu
	left join merk on merk.merk=pn.merk
	left join kapal k on k.kode_kapal=kn.kode_kapal
	left join customer c on merk.kode_cust=c.kode_cust
	left join toko toko on toko.kode_toko=dari_toko
	where to_char(pn.tanggal, 'yyyy-MM-dd')>= v_tgl1 
	and to_char(pn.tanggal, 'yyyy-MM-dd') <=v_tgl2
	and c.kota Like v_kota ||'%'
	and dari_toko iLike v_dari ||'%'
	and kirim_ke iLike v_tujuan ||'%'
	order by pnd.no_penerimaan, pnd.id
	--select * from penerimaan
loop
	return next r;
end loop;

/*
select * from customer
select * from fn_rpt_penerimaan_per_tanggal_kota_toko_tujuan('2008-04-01','2008-04-03', '0001', '000207', '') as 
(no_penerimaan varchar, tgl_masuk text, 
toko varchar, merk_toko text, container varchar, coli float8, kode_barang varchar, nama_barang varchar,ket_paket text, panjang double precision, lebar double precision, 
tinggi double precision, ukuran text, vol double precision, keterangan text, user_ins varchar)
*/
return;
end
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION fn_rpt_penerimaan_per_tanggal_kota_v1(character varying, character varying, character varying) OWNER TO postgres;
