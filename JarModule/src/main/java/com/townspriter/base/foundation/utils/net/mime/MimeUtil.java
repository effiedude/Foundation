package com.townspriter.base.foundation.utils.net.mime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import com.townspriter.base.foundation.utils.text.StringUtil;
import android.webkit.MimeTypeMap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Foundation:MimeUtil
 * @version 1.0.0.0
 * @describe
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date 2021-05-30 18:15:23
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class MimeUtil
{
    public static final String MIMExTYPExUCT="resource/uct";
    public static final String MIMExTYPExUCW="resource/ucw";
    public static final String MIMExTYPExUPP="resource/upp";
    public static final String MIMExTYPExLANG="resource/ucl";
    public static final String MIMExTYPExUCS="video/ucs";
    public static final String MIMExTYPExAPK="application/vnd.android.package-archive";
    public static final String MIMExTYPExMPEG="video/mpeg";
    public static final String MIMExTYPExMP4="video/mp4";
    public static final String MIMExTYPExQUICKxTIME="video/quicktime";
    public static final String MIMExTYPExASF="video/x-ms-asf";
    public static final String MIMETYPExWMV="video/x-ms-wmv";
    public static final String MIMETYPExAVI="video/x-msvideo";
    public static final String MIMETYPEx3GP="video/3gpp";
    public static final String MIMETYPExMOIVE="video/x-sgi-movie";
    public static final String MIMETYPExTEXT="text/plain";
    public static final String MIMETYPExAUDIO="audio/mpeg";
    public static final String MIMExTYPExWMA="audio/x-ms-wma";
    public static final String MIMExTYPExREALAUDIO="audio/x-pn-realaudio";
    public static final String MIMExTYPExWAV="audio/x-wav";
    public static final String MIMETYPExMIDI="audio/midi";
    public static final String MIMETYPExM3U8="application/vnd.apple.mpegurl";
    public static final String MIMETYPExM3U82="application/x-mpegurl";
    public static final String MIMETYPExFLV="video/x-flv";
    public static final String MIMETYPExRMVB="video/vnd.rn-realvideo";
    public static final String MIMETYPEx3GP2="video/3gpp2";
    public static final String MIMETYPExM4V="video/x-m4v";
    public static final String MIMETYPExH264="video/h264";
    public static final String MIMETYPExH263="video/h263";
    public static final String MIMETYPExTS="video/MP2T";
    public static final String MIMETYPExMKV="video/x-matroska";
    public static final String MIMETYPExXVID="video/x-xvid";
    public static final String MIMETYPExVP6="video/x-vp6";
    public static final String MIMETYPExTP="video/tp";
    public static final String MIMExTYPExGIF="image/gif";
    public static final String MIMExTYPExF4V="video/x-f4v";
    public static final String MIMExTYPExDOC="application/msword";
    public static final String MIMExTYPExPPT="application/vnd.ms-powerpoint";
    public static final String MIMExTYPExXLS="application/vnd.ms-excel";
    public static final String MIMExTYPExPDF="application/pdf";
    public static final String MIMExTYPExXLSX="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MIMExTYPExDOCX="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String MIMExTYPExPPTX="application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final HashSet<String> VIDEOxEXTENSIONS=new HashSet<>(64);
    private static final MimeUtil mInstance=new MimeUtil();
    private static final List<String> SYSTEMxNORMALxSUPPORTEDxVIDEOxTYPE=new ArrayList<>(Arrays.asList(MIMExTYPExMPEG,MIMExTYPExMP4,MIMExTYPExQUICKxTIME,MIMExTYPExASF,MIMETYPExWMV,MIMETYPExAVI,MIMETYPEx3GP,MIMETYPExMOIVE));
    static
    {
        VIDEOxEXTENSIONS.add("m1v");
        VIDEOxEXTENSIONS.add("mp2");
        VIDEOxEXTENSIONS.add("mpe");
        VIDEOxEXTENSIONS.add("mpeg");
        VIDEOxEXTENSIONS.add("mp4");
        VIDEOxEXTENSIONS.add("m4v");
        VIDEOxEXTENSIONS.add("3gp");
        VIDEOxEXTENSIONS.add("3gpp");
        VIDEOxEXTENSIONS.add("3g2");
        VIDEOxEXTENSIONS.add("3gpp2");
        VIDEOxEXTENSIONS.add("mkv");
        VIDEOxEXTENSIONS.add("webm");
        VIDEOxEXTENSIONS.add("mts");
        VIDEOxEXTENSIONS.add("ts");
        VIDEOxEXTENSIONS.add("tp");
        VIDEOxEXTENSIONS.add("wmv");
        VIDEOxEXTENSIONS.add("asf");
        VIDEOxEXTENSIONS.add("flv");
        VIDEOxEXTENSIONS.add("asx");
        VIDEOxEXTENSIONS.add("f4v");
        VIDEOxEXTENSIONS.add("hlv");
        VIDEOxEXTENSIONS.add("mov");
        VIDEOxEXTENSIONS.add("qt");
        VIDEOxEXTENSIONS.add("rm");
        VIDEOxEXTENSIONS.add("rmvb");
        VIDEOxEXTENSIONS.add("vob");
        VIDEOxEXTENSIONS.add("avi");
        VIDEOxEXTENSIONS.add("ogv");
        VIDEOxEXTENSIONS.add("ogg");
        VIDEOxEXTENSIONS.add("viv");
        VIDEOxEXTENSIONS.add("vivo");
        VIDEOxEXTENSIONS.add("wtv");
        VIDEOxEXTENSIONS.add("avs");
        VIDEOxEXTENSIONS.add("yuv");
        VIDEOxEXTENSIONS.add("m3u8");
        VIDEOxEXTENSIONS.add("m3u");
        VIDEOxEXTENSIONS.add("bdv");
        VIDEOxEXTENSIONS.add("vdat");
        VIDEOxEXTENSIONS.add("m4a");
        VIDEOxEXTENSIONS.add("mj2");
        VIDEOxEXTENSIONS.add("mpg");
        VIDEOxEXTENSIONS.add("vobsub");
        VIDEOxEXTENSIONS.add("evo");
        VIDEOxEXTENSIONS.add("m2ts");
        VIDEOxEXTENSIONS.add("ssif");
        VIDEOxEXTENSIONS.add("mpegts");
        VIDEOxEXTENSIONS.add("h264");
        VIDEOxEXTENSIONS.add("h263");
        VIDEOxEXTENSIONS.add("m2v");
    }
    private final HashMap<String,String> mMimeTypeToExtension=new HashMap<>(512);
    private final HashMap<String,String> mExtensionToMimeType=new HashMap<>(512);
    
    private MimeUtil()
    {
        loadEntry(MIMExTYPExUCS,"ucs");
        loadEntry(MIMExTYPExUCT,"uct");
        loadEntry(MIMExTYPExUCW,"ucw");
        loadEntry(MIMExTYPExLANG,"ucl");
        loadEntry(MIMExTYPExUPP,"upp");
        loadEntry(MIMETYPExFLV,"flv");
        loadEntry("application/x-shockwave-flash","swf");
        loadEntry("text/vnd.sun.j2me.app-descriptor","jad");
        loadEntry("aplication/java-archive","jar");
        loadEntry(MIMExTYPExDOC,"doc");
        loadEntry(MIMExTYPExDOC,"dot");
        loadEntry(MIMExTYPExXLS,"xls");
        loadEntry(MIMExTYPExPPT,"pps");
        loadEntry(MIMExTYPExPPT,"ppt");
        loadEntry(MIMExTYPExXLSX,"xlsx");
        loadEntry(MIMExTYPExDOCX,"docx");
        loadEntry(MIMExTYPExPPTX,"pptx");
        loadEntry("text/calendar","ics");
        loadEntry("text/calendar","icz");
        loadEntry("text/comma-separated-values","csv");
        loadEntry("text/css","css");
        loadEntry("text/h323","323");
        loadEntry("text/iuls","uls");
        loadEntry("text/mathml","mml");
        loadEntry(MIMETYPExTEXT,"txt");
        loadEntry(MIMETYPExTEXT,"ini");
        loadEntry(MIMETYPExTEXT,"asc");
        loadEntry(MIMETYPExTEXT,"text");
        loadEntry(MIMETYPExTEXT,"diff");
        loadEntry(MIMETYPExTEXT,"log");
        loadEntry(MIMETYPExTEXT,"ini");
        loadEntry(MIMETYPExTEXT,"log");
        loadEntry(MIMETYPExTEXT,"pot");
        loadEntry("application/umd","umd");
        loadEntry("text/xml","xml");
        loadEntry("text/html","html");
        loadEntry("text/html","xhtml");
        loadEntry("text/html","htm");
        loadEntry("text/html","asp");
        loadEntry("text/html","php");
        loadEntry("text/html","jsp");
        loadEntry("text/xml","wml");
        loadEntry("text/richtext","rtx");
        loadEntry("text/rtf","rtf");
        loadEntry("text/texmacs","ts");
        loadEntry("text/text","phps");
        loadEntry("text/tab-separated-values","tsv");
        loadEntry("text/x-bibtex","bib");
        loadEntry("text/x-boo","boo");
        loadEntry("text/x-c++hdr","h++");
        loadEntry("text/x-c++hdr","hpp");
        loadEntry("text/x-c++hdr","hxx");
        loadEntry("text/x-c++hdr","hh");
        loadEntry("text/x-c++src","c++");
        loadEntry("text/x-c++src","cpp");
        loadEntry("text/x-c++src","cxx");
        loadEntry("text/x-chdr","h");
        loadEntry("text/x-component","htc");
        loadEntry("text/x-csh","csh");
        loadEntry("text/x-csrc","c");
        loadEntry("text/x-dsrc","d");
        loadEntry("text/x-haskell","hs");
        loadEntry("text/x-java","java");
        loadEntry("text/x-literate-haskell","lhs");
        loadEntry("text/x-moc","moc");
        loadEntry("text/x-pascal","p");
        loadEntry("text/x-pascal","pas");
        loadEntry("text/x-pcs-gcd","gcd");
        loadEntry("text/x-setext","etx");
        loadEntry("text/x-tcl","tcl");
        loadEntry("text/x-tex","tex");
        loadEntry("text/x-tex","ltx");
        loadEntry("text/x-tex","sty");
        loadEntry("text/x-tex","cls");
        loadEntry("text/x-vcalendar","vcs");
        loadEntry("text/x-vcard","vcf");
        loadEntry("application/andrew-inset","ez");
        loadEntry("application/dsptype","tsp");
        loadEntry("application/futuresplash","spl");
        loadEntry("application/hta","hta");
        loadEntry("application/mac-binhex40","hqx");
        loadEntry("application/mac-compactpro","cpt");
        loadEntry("application/mathematica","nb");
        loadEntry("application/msaccess","mdb");
        loadEntry("application/oda","oda");
        loadEntry("application/ogg","ogg");
        loadEntry(MIMExTYPExPDF,"pdf");
        loadEntry("application/pgp-keys","key");
        loadEntry("application/pgp-signature","pgp");
        loadEntry("application/pics-rules","prf");
        loadEntry("application/rar","rar");
        loadEntry("application/rdf+xml","rdf");
        loadEntry("application/rss+xml","rss");
        loadEntry("application/zip","zip");
        loadEntry(MIMExTYPExAPK,"apk");
        loadEntry("application/vnd.cinderella","cdy");
        loadEntry("application/vnd.ms-pki.stl","stl");
        loadEntry("application/vnd.oasis.opendocument.database","odb");
        loadEntry("application/vnd.oasis.opendocument.formula","odf");
        loadEntry("application/vnd.oasis.opendocument.graphics","odg");
        loadEntry("application/vnd.oasis.opendocument.graphics-template","otg");
        loadEntry("application/vnd.oasis.opendocument.image","odi");
        loadEntry("application/vnd.oasis.opendocument.spreadsheet","ods");
        loadEntry("application/vnd.oasis.opendocument.spreadsheet-template","ots");
        loadEntry("application/vnd.oasis.opendocument.text","odt");
        loadEntry("application/vnd.oasis.opendocument.text-master","odm");
        loadEntry("application/vnd.oasis.opendocument.text-template","ott");
        loadEntry("application/vnd.oasis.opendocument.text-web","oth");
        loadEntry("application/vnd.rim.cod","cod");
        loadEntry("application/vnd.smaf","mmf");
        loadEntry("application/vnd.stardivision.calc","sdc");
        loadEntry("application/vnd.stardivision.draw","sda");
        loadEntry("application/vnd.stardivision.impress","sdd");
        loadEntry("application/vnd.stardivision.impress","sdp");
        loadEntry("application/vnd.stardivision.math","smf");
        loadEntry("application/vnd.stardivision.writer","sdw");
        loadEntry("application/vnd.stardivision.writer","vor");
        loadEntry("application/vnd.stardivision.writer-global","sgl");
        loadEntry("application/vnd.sun.xml.calc","sxc");
        loadEntry("application/vnd.sun.xml.calc.template","stc");
        loadEntry("application/vnd.sun.xml.draw","sxd");
        loadEntry("application/vnd.sun.xml.draw.template","std");
        loadEntry("application/vnd.sun.xml.impress","sxi");
        loadEntry("application/vnd.sun.xml.impress.template","sti");
        loadEntry("application/vnd.sun.xml.math","sxm");
        loadEntry("application/vnd.sun.xml.writer","sxw");
        loadEntry("application/vnd.sun.xml.writer.global","sxg");
        loadEntry("application/vnd.sun.xml.writer.template","stw");
        loadEntry("application/vnd.visio","vsd");
        loadEntry("application/x-abiword","abw");
        loadEntry("application/x-apple-diskimage","dmg");
        loadEntry("application/x-bcpio","bcpio");
        loadEntry("application/x-bittorrent","torrent");
        loadEntry("application/x-cdf","cdf");
        loadEntry("application/x-cdlink","vcd");
        loadEntry("application/x-chess-pgn","pgn");
        loadEntry("application/x-cpio","cpio");
        loadEntry("application/x-debian-package","deb");
        loadEntry("application/x-debian-package","udeb");
        loadEntry("application/x-director","dcr");
        loadEntry("application/x-director","dir");
        loadEntry("application/x-director","dxr");
        loadEntry("application/x-dms","dms");
        loadEntry("application/x-doom","wad");
        loadEntry("application/x-dvi","dvi");
        loadEntry("application/x-flac","flac");
        loadEntry("application/x-font","pfa");
        loadEntry("application/x-font","pfb");
        loadEntry("application/x-font","gsf");
        loadEntry("application/x-font","pcf");
        loadEntry("application/x-font","pcf.Z");
        loadEntry("application/x-freemind","mm");
        loadEntry("application/x-futuresplash","spl");
        loadEntry("application/x-gnumeric","gnumeric");
        loadEntry("application/x-go-sgf","sgf");
        loadEntry("application/x-graphing-calculator","gcf");
        loadEntry("application/x-gtar","gtar");
        loadEntry("application/x-gtar","tgz");
        loadEntry("application/x-gtar","taz");
        loadEntry("application/x-hdf","hdf");
        loadEntry("application/x-ica","ica");
        loadEntry("application/x-internet-signup","ins");
        loadEntry("application/x-internet-signup","isp");
        loadEntry("application/x-iphone","iii");
        loadEntry("application/x-iso9660-image","iso");
        loadEntry("application/x-jmol","jmz");
        loadEntry("application/x-kchart","chrt");
        loadEntry("application/x-killustrator","kil");
        loadEntry("application/x-koan","skp");
        loadEntry("application/x-koan","skd");
        loadEntry("application/x-koan","skt");
        loadEntry("application/x-koan","skm");
        loadEntry("application/x-kpresenter","kpr");
        loadEntry("application/x-kpresenter","kpt");
        loadEntry("application/x-kspread","ksp");
        loadEntry("application/x-kword","kwd");
        loadEntry("application/x-kword","kwt");
        loadEntry("application/x-latex","latex");
        loadEntry("application/x-lha","lha");
        loadEntry("application/x-lzh","lzh");
        loadEntry("application/x-lzx","lzx");
        loadEntry("application/x-maker","frm");
        loadEntry("application/x-maker","maker");
        loadEntry("application/x-maker","frame");
        loadEntry("application/x-maker","fb");
        loadEntry("application/x-maker","book");
        loadEntry("application/x-maker","fbdoc");
        loadEntry("application/x-mif","mif");
        loadEntry("application/x-ms-wmd","wmd");
        loadEntry("application/x-ms-wmz","wmz");
        loadEntry("application/x-msi","msi");
        loadEntry("application/x-ns-proxy-autoconfig","pac");
        loadEntry("application/x-nwc","nwc");
        loadEntry("application/x-object","o");
        loadEntry("application/x-oz-application","oza");
        loadEntry("application/x-pkcs7-certreqresp","p7r");
        loadEntry("application/x-pkcs7-crl","crl");
        loadEntry("application/x-quicktimeplayer","qtl");
        loadEntry("application/x-shar","shar");
        loadEntry("application/x-stuffit","sit");
        loadEntry("application/x-sv4cpio","sv4cpio");
        loadEntry("application/x-sv4crc","sv4crc");
        loadEntry("application/x-tar","tar");
        loadEntry("application/x-texinfo","texinfo");
        loadEntry("application/x-texinfo","texi");
        loadEntry("application/x-troff","t");
        loadEntry("application/x-troff","roff");
        loadEntry("application/x-troff-man","man");
        loadEntry("application/x-ustar","ustar");
        loadEntry("application/x-wais-source","src");
        loadEntry("application/x-wingz","wz");
        loadEntry("application/x-webarchive","webarchive");
        loadEntry("application/x-x509-ca-cert","crt");
        loadEntry("application/x-xcf","xcf");
        loadEntry("application/x-xfig","fig");
        loadEntry("application/epub","epub");
        loadEntry("audio/basic","snd");
        loadEntry(MIMETYPExMIDI,"mid");
        loadEntry(MIMETYPExMIDI,"midi");
        loadEntry(MIMETYPExMIDI,"kar");
        loadEntry(MIMETYPExAUDIO,"mpga");
        loadEntry(MIMETYPExAUDIO,"mpega");
        loadEntry(MIMETYPExAUDIO,"mp2");
        loadEntry(MIMETYPExAUDIO,"mp3");
        loadEntry(MIMETYPExAUDIO,"apu");// 国际版独有
        loadEntry(MIMETYPExAUDIO,"m4a");
        loadEntry("audio/mpegurl","m3u");
        loadEntry("audio/prs.sid","sid");
        loadEntry("audio/x-aiff","aif");
        loadEntry("audio/x-aiff","aiff");
        loadEntry("audio/x-aiff","aifc");
        loadEntry("audio/x-gsm","gsm");
        loadEntry("audio/x-mpegurl","m3u");
        loadEntry(MIMExTYPExWMA,"wma");
        loadEntry("audio/x-ms-wax","wax");
        loadEntry("audio/AMR","amr");
        loadEntry(MIMExTYPExREALAUDIO,"ra");
        loadEntry(MIMExTYPExREALAUDIO,"rm");
        loadEntry(MIMExTYPExREALAUDIO,"ram");
        loadEntry("audio/x-realaudio","ra");
        loadEntry("audio/x-scpls","pls");
        loadEntry("audio/x-sd2","sd2");
        loadEntry(MIMExTYPExWAV,"wav");
        loadEntry("image/bmp","bmp");
        loadEntry("image/gif","gif");
        loadEntry("image/ico","cur");
        loadEntry("image/ico","ico");
        loadEntry("image/ief","ief");
        loadEntry("image/jpeg","jpeg");
        loadEntry("image/jpeg","jpg");
        loadEntry("image/jpeg","jpe");
        loadEntry("image/pcx","pcx");
        loadEntry("image/png","png");
        loadEntry("image/svg+xml","svg");
        loadEntry("image/svg+xml","svgz");
        loadEntry("image/tiff","tiff");
        loadEntry("image/tiff","tif");
        loadEntry("image/vnd.djvu","djvu");
        loadEntry("image/vnd.djvu","djv");
        loadEntry("image/vnd.wap.wbmp","wbmp");
        loadEntry("image/x-cmu-raster","ras");
        loadEntry("image/x-coreldraw","cdr");
        loadEntry("image/x-coreldrawpattern","pat");
        loadEntry("image/x-coreldrawtemplate","cdt");
        loadEntry("image/x-corelphotopaint","cpt");
        loadEntry("image/x-icon","ico");
        loadEntry("image/x-jg","art");
        loadEntry("image/x-jng","jng");
        loadEntry("image/x-ms-bmp","bmp");
        loadEntry("image/x-photoshop","psd");
        loadEntry("image/x-portable-anymap","pnm");
        loadEntry("image/x-portable-bitmap","pbm");
        loadEntry("image/x-portable-graymap","pgm");
        loadEntry("image/x-portable-pixmap","ppm");
        loadEntry("image/x-rgb","rgb");
        loadEntry("image/x-xbitmap","xbm");
        loadEntry("image/x-xpixmap","xpm");
        loadEntry("image/x-xwindowdump","xwd");
        loadEntry("model/iges","igs");
        loadEntry("model/iges","iges");
        loadEntry("model/mesh","msh");
        loadEntry("model/mesh","mesh");
        loadEntry("model/mesh","silo");
        loadEntry("text/calendar","ics");
        loadEntry("text/calendar","icz");
        loadEntry("text/comma-separated-values","csv");
        loadEntry("text/css","css");
        loadEntry("text/h323","323");
        loadEntry("text/iuls","uls");
        loadEntry("text/mathml","mml");
        loadEntry(MIMETYPExTEXT,"txt");
        loadEntry(MIMETYPExTEXT,"asc");
        loadEntry(MIMETYPExTEXT,"text");
        loadEntry(MIMETYPExTEXT,"diff");
        loadEntry(MIMETYPExTEXT,"pot");
        loadEntry(MIMETYPExTEXT,"umd");
        loadEntry("text/richtext","rtx");
        loadEntry("text/rtf","rtf");
        loadEntry("text/texmacs","ts");
        loadEntry("text/text","phps");
        loadEntry("text/tab-separated-values","tsv");
        loadEntry("text/x-bibtex","bib");
        loadEntry("text/x-boo","boo");
        loadEntry("text/x-c++hdr","h++");
        loadEntry("text/x-c++hdr","hpp");
        loadEntry("text/x-c++hdr","hxx");
        loadEntry("text/x-c++hdr","hh");
        loadEntry("text/x-c++src","c++");
        loadEntry("text/x-c++src","cpp");
        loadEntry("text/x-c++src","cxx");
        loadEntry("text/x-chdr","h");
        loadEntry("text/x-component","htc");
        loadEntry("text/x-csh","csh");
        loadEntry("text/x-csrc","c");
        loadEntry("text/x-dsrc","d");
        loadEntry("text/x-haskell","hs");
        loadEntry("text/x-java","java");
        loadEntry("text/x-literate-haskell","lhs");
        loadEntry("text/x-moc","moc");
        loadEntry("text/x-pascal","p");
        loadEntry("text/x-pascal","pas");
        loadEntry("text/x-pcs-gcd","gcd");
        loadEntry("text/x-setext","etx");
        loadEntry("text/x-tcl","tcl");
        loadEntry("text/x-tex","tex");
        loadEntry("text/x-tex","ltx");
        loadEntry("text/x-tex","sty");
        loadEntry("text/x-tex","cls");
        loadEntry("text/x-vcalendar","vcs");
        loadEntry("text/x-vcard","vcf");
        loadEntry(MIMETYPEx3GP,"3gp");
        loadEntry(MIMETYPEx3GP,"3g2");
        loadEntry("video/dl","dl");
        loadEntry("video/dv","dif");
        loadEntry("video/dv","dv");
        loadEntry("video/fli","fli");
        loadEntry(MIMExTYPExMPEG,"mpeg");
        loadEntry(MIMExTYPExMPEG,"mpg");
        loadEntry(MIMExTYPExMPEG,"mpe");
        loadEntry(MIMExTYPExMPEG,"VOB");
        loadEntry(MIMExTYPExMP4,"mp4");
        /* 离线视频专用格式 */
        loadEntry(MIMExTYPExMP4,"vdat");
        loadEntry(MIMExTYPExQUICKxTIME,"qt");
        loadEntry(MIMExTYPExQUICKxTIME,"mov");
        loadEntry("video/vnd.mpegurl","mxu");
        loadEntry("video/x-la-asf","lsf");
        loadEntry("video/x-la-asf","lsx");
        loadEntry("video/x-mng","mng");
        loadEntry(MIMExTYPExASF,"asf");
        loadEntry(MIMExTYPExASF,"asx");
        loadEntry("video/x-ms-wm","wm");
        loadEntry(MIMETYPExWMV,"wmv");
        loadEntry("video/x-ms-wmx","wmx");
        loadEntry("video/x-ms-wvx","wvx");
        loadEntry(MIMETYPExAVI,"avi");
        loadEntry(MIMETYPExMOIVE,"movie");
        loadEntry("x-conference/x-cooltalk","ice");
        loadEntry("x-epoc/x-sisx-app","sisx");
        loadEntry("application/vnd.apple.mpegurl","m3u8");
        loadEntry(MIMETYPExRMVB,"rmvb");
        loadEntry(MIMETYPExRMVB,"rm");
        loadEntry("video/x-matroska","mkv");
        loadEntry("video/x-f4v","f4v");
        loadEntry("audio/aac","aac");
    }
    
    public static MimeUtil getInstance()
    {
        return mInstance;
    }
    
    public static int getMediaTypeByPath(@NonNull String mediaLocalPath)
    {
        int mediaType=-1;
        String mimeType=guessMediaMimeType(mediaLocalPath);
        boolean isImageType,isAudioType,isVideoType;
        isImageType=isImageType(mimeType);
        isAudioType=isAudioType(mimeType);
        isVideoType=isVideoType(mimeType);
        if(isImageType)
        {
            mediaType=0;
        }
        if(isAudioType)
        {
            mediaType=1;
        }
        if(isVideoType)
        {
            mediaType=2;
        }
        return mediaType;
    }
    
    public static @Nullable String guessMediaMimeType(@NonNull String filePath)
    {
        String mimeType=null;
        // 使用系统接口获取目标路径中文件的后缀名(扩展名)
        String extension=MimeTypeMap.getFileExtensionFromUrl(filePath);
        if(!StringUtil.isEmptyWithNull(extension))
        {
            // 文件扩展名与系统哈系表进行匹配
            mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if(extension!=null)
        {
            mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            /** 系统方法不支持包含中文字符的名称检测.提供兜底策略.获取文件的后缀信息再次获取文件类型 */
            if(mimeType==null)
            {
                extension=filePath.substring(filePath.lastIndexOf(".")+1);
                mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }
        return mimeType;
    }
    
    public static String getFileExtensionFromUrl(String url)
    {
        if(url!=null&&url.length()>0)
        {
            int query=url.lastIndexOf('?');
            if(query>0)
            {
                url=url.substring(0,query);
            }
            int filenamePos=url.lastIndexOf('/');
            String filename=0<=filenamePos?url.substring(filenamePos+1):url;
            if(filename.length()>0)
            {
                int dotPos=filename.lastIndexOf('.');
                if(0<=dotPos)
                {
                    return filename.substring(dotPos+1);
                }
            }
        }
        return "";
    }
    
    public static String getFileExtensionFromFileName(String fileName)
    {
        if(fileName!=null&&fileName.length()>0)
        {
            int dotPos=fileName.lastIndexOf('.');
            if(0<=dotPos)
            {
                return fileName.substring(dotPos+1);
            }
        }
        return "";
    }
    
    public static boolean isGifType(String mimeType)
    {
        if(!StringUtil.isEmptyWithTrim(mimeType))
        {
            return MIMExTYPExGIF.equalsIgnoreCase(mimeType)||MIMExTYPExGIF.contains(mimeType);
        }
        return false;
    }
    
    public static boolean isImageType(String mimeType)
    {
        return !StringUtil.isEmptyWithTrim(mimeType)&&mimeType.toLowerCase().contains("image/");
    }
    
    public static boolean isAudioType(String mimeType)
    {
        if(!StringUtil.isEmptyWithTrim(mimeType))
        {
            return MIMExTYPExWMA.equalsIgnoreCase(mimeType)||MIMExTYPExREALAUDIO.equalsIgnoreCase(mimeType)||MIMExTYPExWAV.equalsIgnoreCase(mimeType)||MIMETYPExMIDI.equalsIgnoreCase(mimeType)||MIMETYPExAUDIO.equalsIgnoreCase(mimeType);
        }
        return false;
    }
    
    public static boolean isVideoType(@NonNull String mimeType)
    {
        if(StringUtil.isEmptyWithNull(mimeType))
        {
            return false;
        }
        return mimeType.startsWith("video");
    }
    
    public static boolean isVideoType(String mimeType,String url)
    {
        if(!StringUtil.isEmptyWithTrim(mimeType)&&mimeType.toLowerCase().contains("video/"))
        {
            return true;
        }
        return !StringUtil.isEmptyWithTrim(url)&&isVideoPath(url);
    }
    
    public static boolean isOfficeFileType(String mimeType)
    {
        if(!StringUtil.isEmpty(mimeType))
        {
            return MIMExTYPExDOC.equalsIgnoreCase(mimeType)||MIMExTYPExPPT.equalsIgnoreCase(mimeType)||MIMExTYPExPPTX.equalsIgnoreCase(mimeType)||MIMExTYPExXLS.equalsIgnoreCase(mimeType)||MIMExTYPExXLSX.equalsIgnoreCase(mimeType)||MIMExTYPExDOCX.equalsIgnoreCase(mimeType)||MIMExTYPExPDF.equalsIgnoreCase(mimeType);
        }
        return false;
    }
    
    /** 由于安卓不同的机型支持的格式不一没有统一的标准.这里列出系统一般支持较好的格式 */
    public static boolean isSystemNormalSupportedVideoType(String mimeType)
    {
        return(!StringUtil.isEmptyWithTrim(mimeType)&&SYSTEMxNORMALxSUPPORTEDxVIDEOxTYPE.contains(mimeType));
    }
    
    public static boolean isMP3File(String fileName)
    {
        if(StringUtil.isEmptyWithTrim(fileName))
        {
            return false;
        }
        String extension=getFileExtensionFromUrl(fileName);
        return StringUtil.isNotEmptyWithTrim(extension)&&"mp3".equalsIgnoreCase(extension);
    }
    
    public static boolean isAPKFile(String fileName)
    {
        if(StringUtil.isEmptyWithTrim(fileName))
        {
            return false;
        }
        String extension=getFileExtensionFromUrl(fileName);
        return StringUtil.isNotEmptyWithTrim(extension)&&"apk".equalsIgnoreCase(extension);
    }
    
    public static boolean isPDFFile(String fileName)
    {
        if(StringUtil.isEmptyWithTrim(fileName))
        {
            return false;
        }
        String extension=getFileExtensionFromUrl(fileName);
        return StringUtil.isNotEmptyWithTrim(extension)&&"pdf".equalsIgnoreCase(extension);
    }
    
    public static boolean isAPKMimeType(String mimeType)
    {
        return MIMExTYPExAPK.equalsIgnoreCase(mimeType);
    }
    
    public static boolean isVideoPath(String path)
    {
        if(StringUtil.isEmptyWithTrim(path))
        {
            return false;
        }
        int index=path.indexOf("?");
        if(index>0)
        {
            path=path.substring(0,index);
        }
        int lastDot=path.lastIndexOf(".");
        if(lastDot<=0)
        {
            return false;
        }
        return VIDEOxEXTENSIONS.contains(path.substring(lastDot+1).toLowerCase(Locale.ENGLISH));
    }
    
    public static boolean isVideoExtension(String extension)
    {
        if(StringUtil.isEmpty(extension))
        {
            return false;
        }
        return VIDEOxEXTENSIONS.contains(extension.toLowerCase(Locale.ENGLISH));
    }
    
    public String getMimeTypeFromUrl(String string)
    {
        String mimeType;
        String extension=getFileExtensionFromUrl(string);
        mimeType=getMimeTypeFromExtension(extension);
        return mimeType==null?"":mimeType;
    }
    
    public String getMimeTypeFromExtension(String extension)
    {
        String mimeType="";
        if(extension!=null&&extension.length()>0)
        {
            mimeType=mExtensionToMimeType.get(extension.toLowerCase(Locale.ENGLISH));
        }
        return mimeType==null?"":mimeType;
    }
    
    public HashSet<String> getExtensionFromMimeType(String mimeType)
    {
        HashSet<String> extension=new HashSet<>();
        if(mimeType!=null&&mimeType.length()>0)
        {
            Iterator<Entry<String,String>> iter=mExtensionToMimeType.entrySet().iterator();
            while(iter.hasNext())
            {
                Entry<String,String> entry=iter.next();
                String key=entry.getKey();
                String val=entry.getValue();
                if(mimeType.equalsIgnoreCase(val))
                {
                    extension.add(key);
                }
            }
        }
        return extension;
    }
    
    private void loadEntry(String mimeType,String extension)
    {
        if(!mMimeTypeToExtension.containsKey(mimeType))
        {
            mMimeTypeToExtension.put(mimeType,extension);
        }
        mExtensionToMimeType.put(extension,mimeType);
    }
}
