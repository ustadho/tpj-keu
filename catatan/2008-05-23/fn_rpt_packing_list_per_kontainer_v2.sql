-- Function: fn_rpt_packing_list_per_kontainer_v2(bigint)

-- DROP FUNCTION fn_rpt_packing_list_per_kontainer_v2(bigint);

CREATE OR REPLACE FUNCTION fn_rpt_packing_list_per_kontainer_v2(bigint)
  RETURNS SETOF record AS
$BODY$
declare 
	v_serial_kon alias for $1;
	r record;

begin
for r in
	--select * from penerimaan
	select coalesce(kn.no_spnu,'') as container, coalesce(emkl.nama,'') as emkl,
	case  kn.kondisi when 0 then 'PORT TO DOOR' WHEN 1 then 'PORT TO PORT' else 'LAINNYA' End as kondisi,
	--case  pn.metode_pengiriman when 'PD' then 'PORT TO DOOR' WHEN 'PP' then 'PORT TO PORT' else 'LAINNYA' End as kondisi,
	coalesce(nama_kapal,'') as nama_kapal, fn_tanggal_ind(tgl_berangkat) as tgl_berangkat, --coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'),'') as tgl_berangkat,
	merk.kode_cust as kode_toko_tujuan, coalesce(c.nama,'') as nama_toko_tujuan,
	merk.merk,
	merk.merk ||' ('||coalesce(c.nama,'')||') ' as merk_toko,
	coalesce(c.alamat_1,'') as alamat,
	exd.no_penerimaan, to_char(pn.tanggal,'dd-MM-yy') as tgl_masuk, coalesce(toko.nama,'') as toko,
	fn_jumlah_coli_kontainer_v2(exd.no_penerimaan, v_serial_kon) as total_coli,
	coalesce(exd.coli,0) as coli, exd.kode_barang , (nama_barang ||fn_ket_pindah_kontainer_v2(v_serial_kon, serial))::varchar, 
	case when exd.is_paket then 'Paket' else '' end as ket_paket,
	coalesce(exd.panjang,0) as panjang, coalesce(exd.lebar,0) as lebar, coalesce(exd.tinggi,0) as tingi,
	case when exd.is_paket then 'Paket' else 
		case 	when exd.lebar=0 and exd.tinggi=0 then exd.panjang::text
			when exd.tinggi=0 then exd.panjang::varchar ||' x '|| exd.lebar::varchar
			when exd.lebar>0 and exd.tinggi>0 and exd.panjang>0 then exd.panjang::varchar ||' x '|| exd.lebar::varchar ||' x ' ||exd.tinggi::varchar
		end
	end as ukuran,
	--(fn_get_volume(coalesce(exd.panjang,0), coalesce(exd.lebar,0), coalesce(exd.tinggi,0), coalesce(exd.coli,0))::NUMERIC(15, 4))::float4 as vol, 
	case when is_fix_vol =true then coalesce(fix_vol,0) else fn_get_volume(coalesce(exd.panjang,0), coalesce(exd.lebar,0), coalesce(exd.tinggi,0), coalesce(exd.coli,0)) end as vol, 
	coalesce(exd.keterangan,'') as keterangan
	from expedisi_barang exd
	inner join penerimaan_detail pnd on pnd.id=exd.serial
	inner join kontainer kn on exd.no_spnu=kn.no_spnu
	left join kapal kpl on kpl.kode_kapal=kn.kode_kapal
	left join penerimaan pn on pn.no_penerimaan=exd.no_penerimaan--pn.no_container=kn.no_spnu 
	left join barang b on b.kode_barang=exd.kode_barang
	left join merk on merk.merk=pn.merk
	left join customer c on c.kode_cust=merk.kode_cust
	left join emkl on emkl.kode_emkl=kn.emkl
	left join toko on toko.kode_toko=pn.dari_toko
	where coalesce(kn.serial_kon, 0) = v_serial_kon
	order by coalesce(kn.no_spnu,'') , coalesce(emkl.nama,'') ,
	coalesce(nama_kapal,'') , coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'),''), merk.merk ||' ('||coalesce(c.nama,'')||') ',
	case  kn.kondisi when 0 then 'PORT TO DOOR' WHEN 1 then 'PORT TO PORT' else 'LAINNYA' End,
	to_char(pn.tanggal,'dd-MM-yy'), pnd.no_penerimaan, serial
loop
	return next r;
end loop;

/*
select * from customer
select * from fn_rpt_packing_list_per_kontainer_v2(670) as 
(container varchar,  emkl varchar, kondisi text, nama_kapal varchar, tgl_berangkat varchar, kode_toko_tujuan varchar, nama_toko_tujuan varchar, merk varchar, 
merk_toko text, alamat varchar,no_penerimaan varchar, tgl_masuk text, 
toko varchar, total_coli float8, coli float8, kode_barang varchar, nama_barang varchar, ket_paket text, panjang double precision, lebar double precision, 
tinggi double precision, ukuran text, vol double precision, keterangan text)


*/
return;
end
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION fn_rpt_packing_list_per_kontainer_v2(bigint) OWNER TO postgres;
