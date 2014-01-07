-- Function: fn_rpt_packing_list_per_merk10(character varying, character varying, character varying)

-- DROP FUNCTION fn_rpt_packing_list_per_merk10(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION fn_rpt_packing_list_per_merk10(character varying, character varying, character varying)
  RETURNS SETOF record AS
$BODY$
declare 
	v_tgl_1 alias for $1;
	v_tgl_2 alias for $2;
	v_merk alias for $3;
	v_qry varchar;
	r record;

begin

v_qry:='select 
	case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End as kondisi,
	coalesce(nama_kapal,'''') as nama_kapal, fn_tanggal_ind(tgl_berangkat) as tgl_berangkat, --coalesce(to_char(tgl_berangkat,''dd-MM-yyyy''),'''') as tgl_berangkat,
	case when satuan_kirim=''LCL'' then ''LCL'' else ''1 CONTAINER'' end as satuan_kirim,   
	merk.merk ||'' (''||coalesce(c.nama,'''')||'') '' as merk_toko,
	coalesce(kn.no_spnu,'''') as container, coalesce(emkl.nama,'''') as emkl,
	pnd.no_penerimaan, to_char(pn.tanggal,''dd-MM-yy'') as tgl_masuk, coalesce(toko.nama,'''') as toko,
	fn_jumlah_coli(pnd.no_penerimaan) as total_coli,
	sum(coalesce(pnd.coli,0)) as coli, pnd.kode_barang , nama_barang , 
	case when pnd.is_paket then ''Paket'' else '''' end as ket_paket,
	coalesce(pnd.panjang,0) as panjang, coalesce(pnd.lebar,0) as lebar, coalesce(pnd.tinggi,0) as tingi,
	case when pnd.is_paket then ''Paket'' else 
		case 	when pnd.lebar=0 and pnd.tinggi=0 then pnd.panjang::text
			when pnd.tinggi=0 then pnd.panjang::varchar ||'' x ''|| pnd.lebar::varchar
			when pnd.lebar>0 and pnd.tinggi>0 and pnd.panjang>0 then pnd.panjang::varchar ||'' x ''|| pnd.lebar::varchar ||'' x '' ||pnd.tinggi::varchar
		end
	end as ukuran,
	fn_get_volume(coalesce(pnd.panjang,0), coalesce(pnd.lebar,0), coalesce(pnd.tinggi,0), sum(coalesce(pnd.coli,0))) as vol, 
	coalesce(pnd.keterangan,'''') as keterangan
	from expedisi_barang pnd
	left join kontainer kn on kn.no_spnu=pnd.no_spnu
	left join kapal kpl on kpl.kode_kapal=kn.kode_kapal
	inner join penerimaan pn on pn.no_container=kn.no_spnu and pn.no_penerimaan=pnd.no_penerimaan
	inner join barang b on b.kode_barang=pnd.kode_barang
	left join merk on merk.merk=pn.merk
	left join customer c on c.kode_cust=merk.kode_cust
	left join emkl on emkl.kode_emkl=kn.emkl
	left join toko on toko.kode_toko=pn.dari_toko
	where to_char(pn.tanggal,''yyyy-MM-dd'') >= '||''''||v_tgl_1||''''||' and to_char(pn.tanggal,''yyyy-MM-dd'') <='||''''||v_tgl_2||''''||'
	and coalesce(pn.merk,'''') in ( '||''''||v_merk||''''||')
	group by
	case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End ,
	coalesce(nama_kapal,'''') , fn_tanggal_ind(tgl_berangkat), 
	case when satuan_kirim=''LCL'' then ''LCL'' else ''1 CONTAINER'' end ,   
	merk.merk ||'' (''||coalesce(c.nama,'''')||'') '' ,
	coalesce(kn.no_spnu,'''') , coalesce(emkl.nama,'''') ,
	pnd.no_penerimaan, to_char(pn.tanggal,''dd-MM-yy'') , coalesce(toko.nama,'''') ,
	fn_jumlah_coli(pnd.no_penerimaan) ,
	pnd.kode_barang , nama_barang , 
	case when pnd.is_paket then ''Paket'' else '''' end ,
	coalesce(pnd.panjang,0) , coalesce(pnd.lebar,0) , coalesce(pnd.tinggi,0) ,
	case when pnd.is_paket then ''Paket'' else 
		case 	when pnd.lebar=0 and pnd.tinggi=0 then pnd.panjang::text
			when pnd.tinggi=0 then pnd.panjang::varchar ||'' x ''|| pnd.lebar::varchar
			when pnd.lebar>0 and pnd.tinggi>0 and pnd.panjang>0 then pnd.panjang::varchar ||'' x ''|| pnd.lebar::varchar ||'' x '' ||pnd.tinggi::varchar
		end
	end ,
	--fn_get_volume(coalesce(pnd.panjang,0), coalesce(pnd.lebar,0), coalesce(pnd.tinggi,0), sum(coalesce(pnd.coli,0))) , 
	coalesce(pnd.keterangan,''''), pnd.serial 
	order by
	case  kn.kondisi when 0 then ''PORT TO DOOR'' WHEN 1 then ''PORT TO PORT'' else ''LAINNYA'' End ,
	coalesce(nama_kapal,'''') , fn_tanggal_ind(tgl_berangkat), 
	case when satuan_kirim=''LCL'' then ''LCL'' else ''1 CONTAINER'' end ,   
	merk.merk ||'' (''||coalesce(c.nama,'''')||'') '' ,
	coalesce(kn.no_spnu,'''') , coalesce(emkl.nama,'''') ,
	pnd.no_penerimaan, to_char(pn.tanggal,''dd-MM-yy''), pnd.serial ';

for r in execute
	v_qry

loop
	return next r;
end loop;

/*
select * from customer


select * from expedisi_barang where no_penerimaan='0801190001'
*/
return;
end
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION fn_rpt_packing_list_per_merk10(character varying, character varying, character varying) OWNER TO postgres;
