-- Function: fn_rpt_packing_list_per_merk_per_kapal_tgl_berangkat_v2(character varying, character varying, character varying)

-- DROP FUNCTION fn_rpt_packing_list_per_merk_per_kapal_tgl_berangkat_v2(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION fn_rpt_packing_list_per_merk_per_kapal_tgl_berangkat_v2(character varying, character varying, character varying)
  RETURNS SETOF record AS
$BODY$
declare 
	v_merk alias for $1;
	v_kapal alias for $2;
	v_tgl_berangkat alias for $3;
	v_qry varchar;
	r record;

begin
--case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End as kondisi,
v_qry:='select 
	case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End as kondisi,
	coalesce(nama_kapal,'''') as nama_kapal, fn_tanggal_ind(tgl_berangkat) as tgl_berangkat, --coalesce(to_char(tgl_berangkat,''dd-MM-yyyy''),'''') as tgl_berangkat,
	case when satuan_kirim=''LCL'' then ''LCL'' else ''CONTAINER'' end as satuan_kontainer,   
	case when satuan_kirim=''LCL'' then ''LCL'' else coalesce(kn.no_spnu,'''')||'' CONTAINER'' end as satuan_kirim, 
	merk.merk ||'' (''||coalesce(c.nama,'''')||'') '' as merk_toko,
	coalesce(kn.no_spnu,'''') as container, coalesce(emkl.nama,'''') as emkl,
	exd.no_penerimaan, to_char(pn.tanggal,''dd-MM-yy'') as tgl_masuk, coalesce(toko.nama,'''') as toko,
	fn_jumlah_coli(exd.no_penerimaan) as total_coli,
	sum(coalesce(exd.coli,0)) as coli, exd.kode_barang , nama_barang , 
	case when exd.is_paket then ''Paket'' else '''' end as ket_paket,
	coalesce(exd.panjang,0) as panjang, coalesce(exd.lebar,0) as lebar, coalesce(exd.tinggi,0) as tingi,
	case when exd.is_paket then ''Paket'' else 
		case 	when exd.lebar =0 and exd.tinggi=0 and exd.panjang=0 then ''''
			when exd.lebar =0 and exd.tinggi=0 and exd.panjang<>0 then exd.panjang::text
			when exd.lebar<>0 and exd.tinggi=0 and exd.panjang<>0  then exd.panjang::varchar ||'' x ''|| exd.lebar::varchar
			when exd.lebar >0 and exd.tinggi>0 and exd.panjang>0 then exd.panjang::varchar ||'' x ''|| exd.lebar::varchar ||'' x '' ||exd.tinggi::varchar
		end
	end as ukuran,
	case when is_fix_vol =true then coalesce(fix_vol,0) else fn_get_volume(coalesce(exd.panjang,0), coalesce(exd.lebar,0), coalesce(exd.tinggi,0), sum(coalesce(exd.coli,0))) end as vol, 
	coalesce(exd.keterangan,'''') as keterangan
	from expedisi_barang exd
	left join penerimaan_detail pnd on pnd.id=exd.serial
	left join kontainer kn on kn.no_spnu=exd.no_spnu
	left join kapal kpl on kpl.kode_kapal=kn.kode_kapal
	inner join penerimaan pn on pn.no_container=kn.no_spnu and pn.no_penerimaan=exd.no_penerimaan
	inner join barang b on b.kode_barang=exd.kode_barang
	left join merk on merk.merk=pn.merk
	left join customer c on c.kode_cust=merk.kode_cust
	left join emkl on emkl.kode_emkl=kn.emkl
	left join toko on toko.kode_toko=pn.dari_toko
	where coalesce(to_char(kn.tgl_berangkat,''yyyy-MM-dd''), '''') = '||''''||v_tgl_berangkat||''''||' 
	and coalesce(pn.merk,'''') in ( '||''''||v_merk||''''||')
	and coalesce(kn.kode_kapal,'''') iLike ( '||''''||v_kapal||'%'||''''||')
	group by
	case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End ,
	coalesce(nama_kapal,'''') , fn_tanggal_ind(tgl_berangkat), 
	case when satuan_kirim=''LCL'' then ''LCL'' else ''CONTAINER'' end ,   
	case when satuan_kirim=''LCL'' then ''LCL'' else coalesce(kn.no_spnu,'''')||'' CONTAINER'' end,
	merk.merk ||'' (''||coalesce(c.nama,'''')||'') '' ,
	coalesce(kn.no_spnu,'''') , coalesce(emkl.nama,'''') ,
	exd.no_penerimaan, to_char(pn.tanggal,''dd-MM-yy'') , coalesce(toko.nama,'''') ,
	fn_jumlah_coli(exd.no_penerimaan) ,
	exd.kode_barang , nama_barang , 
	case when exd.is_paket then ''Paket'' else '''' end ,
	coalesce(exd.panjang,0) , coalesce(exd.lebar,0) , coalesce(exd.tinggi,0) ,
	case when exd.is_paket then ''Paket'' else 
		case 	when exd.lebar =0 and exd.tinggi=0 and exd.panjang=0 then ''''
			when exd.lebar =0 and exd.tinggi=0 and exd.panjang<>0 then exd.panjang::text
			when exd.lebar<>0 and exd.tinggi=0 and exd.panjang<>0  then exd.panjang::varchar ||'' x ''|| exd.lebar::varchar
			when exd.lebar >0 and exd.tinggi>0 and exd.panjang>0 then exd.panjang::varchar ||'' x ''|| exd.lebar::varchar ||'' x '' ||exd.tinggi::varchar
		end
	end ,
	--fn_get_volume(coalesce(exd.panjang,0), coalesce(exd.lebar,0), coalesce(exd.tinggi,0), sum(coalesce(exd.coli,0))) , 
	coalesce(exd.keterangan,''''), pnd.is_fix_vol, coalesce(fix_vol,0), pnd.id
	order by
	case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End ,
	coalesce(nama_kapal,'''') , fn_tanggal_ind(tgl_berangkat), 
	case when satuan_kirim=''LCL'' then ''LCL'' else ''CONTAINER'' end ,   
	case when satuan_kirim=''LCL'' then ''LCL'' else coalesce(kn.no_spnu,'''')||'' CONTAINER'' end,
	merk.merk ||'' (''||coalesce(c.nama,'''')||'') '' ,
	coalesce(kn.no_spnu,'''') , coalesce(emkl.nama,'''') ,
	exd.no_penerimaan, to_char(pn.tanggal,''dd-MM-yy''), pnd.id ';


raise notice 'Queri : %', v_qry;
 
for r in execute
	v_qry

loop
	return next r;
end loop;

/*

select * from customer
select * from fn_rpt_packing_list_per_merk_per_kapal_tgl_berangkat_v3('ADIT', '000002', '2008-03-03') as 
(kondisi text, nama_kapal varchar, tgl_berangkat varchar, satuan_kirim text, sat_kontainer text, merk_toko text, container varchar, emkl varchar, no_penerimaan varchar, tgl_masuk text, 
toko varchar, total_coli float8, coli float8, kode_barang varchar, nama_barang varchar,ket_paket text, panjang double precision, lebar double precision, 
tinggi double precision, ukuran text, vol double precision, keterangan text)


select * from expedisi_barang where no_penerimaan='0801190001'
*/
return;
end
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION fn_rpt_packing_list_per_merk_per_kapal_tgl_berangkat_v2(character varying, character varying, character varying) OWNER TO postgres;
