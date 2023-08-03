package com.easyback.framework.utils;


import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.date.DateUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * pdf工具类
 * @author zhuangqingdian
 * @date 2019-10-28
 */
@Slf4j
public class PdfUtil {

    /**
     * 根据模板生成pdf
     * @param pdfUrl 模板url
     * @param simpleFormFields Map(String,Object)
     * @return
     */
    public static Boolean createPDF(String pdfUrl, String fileUrl, Map<String, Object> simpleFormFields) {
        PdfReader reader = null;
        AcroFields s = null;
        PdfStamper ps = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpURLConnection urlConnection = null;
        FileOutputStream fos = null;
        String BOUNDARY = "-----------------12345654321-----------";
        try {
//             访问oss获取输入流和pdfReader
            URL url = new URL(pdfUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(30000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            urlConnection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            urlConnection.setRequestProperty("Charset", "UTF-8");
            InputStream input = urlConnection.getInputStream();
//            InputStream input = new FileInputStream("/Users/apple/Downloads/hZN4b92QvJWyIkiS1127_1.pdf");
            reader = new PdfReader(input);

            // 创建流
            fos = new FileOutputStream(fileUrl);
            ps = new PdfStamper(reader, fos);
            s = ps.getAcroFields();

            /**
             * 使用中文字体 使用 AcroFields填充值的不需要在程序中设置字体，在模板文件中设置字体为中文字体 Adobe 宋体 std L
             */
//            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont();

            /**
             * 设置编码格式
             */
            s.addSubstitutionFont(bfChinese);

            // 遍历data 给pdf表单表格赋值
            for (String key : simpleFormFields.keySet()) {
                if (null != simpleFormFields.get(key)) {
                    log.info("key={}", key);
                    log.info("value={}", simpleFormFields.get(key).toString());
                    s.setField(key,simpleFormFields.get(key).toString());
                }
            }

            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            ps.setFormFlattening(true);

            return true;
        } catch (IOException | DocumentException e) {
            System.out.println("读取文件异常");
            e.printStackTrace();
            return false;
        }finally {
            try {
                baos.close();
                ps.close();
                reader.close();
                fos.flush();
                fos.close();
            } catch (IOException | DocumentException e) {
                System.out.println("关闭流异常");
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并两个pdf
     * @param fileUrl 基础pdf
     * @param result 要追加的pdf字节流
     * @param outUrl 输出的pdf路径
     */
    public static void mergePDF(String fileUrl, byte[] result, String outUrl) {

        FileOutputStream out = null;
        Document document = null;
        try {
            PdfReader reader1 = new PdfReader(fileUrl);
            PdfReader reader2 = new PdfReader(result);

            out = new FileOutputStream(outUrl);

            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();
            PdfContentByte cb = writer.getDirectContent();

//            int totalPages = 0;
//            totalPages += reader1.getNumberOfPages();
//            totalPages += reader2.getNumberOfPages();

            java.util.List<PdfReader> readers = new ArrayList<PdfReader>();
            readers.add(reader1);
            readers.add(reader2);

            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // 循环浏览pdf文件并添加到输出中。
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // 在目标中为每个源页创建一个新页。
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
        } catch (IOException | DocumentException e) {
            System.out.println("读取文件异常");
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                document.close();
                out.close();
            } catch (IOException e) {
                System.out.println("关闭流异常");
                e.printStackTrace();
            }
        }

    }



    /**
     * 生成pdf工具类
     * @param htmlStr
     * @param pdfPath
     * @return
     */
    public static Boolean html2Pdf(String htmlStr, String pdfPath) {
        try {

            // 1.新建document
            Document document = new Document();
            // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
            //创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            //设置系统字体
            BaseFont bf = BaseFont.createFont(new ClassPathResource("/Microsoft-YaHei.ttf").getPath(), BaseFont.IDENTITY_H, false);

            //添加页码
            setFooter(writer, bf);

            // 3.打开文档
            document.open();

            //要解析的html
            //html转换成普通文字,方法如下:
            org.jsoup.nodes.Document contentDoc = Jsoup.parseBodyFragment(htmlStr);
            org.jsoup.nodes.Document.OutputSettings outputSettings = new org.jsoup.nodes.Document.OutputSettings();
            outputSettings.syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
            contentDoc.outputSettings(outputSettings);
            String parsedHtml = contentDoc.outerHtml();

            //这儿的font-family不支持汉字，{font-family:仿宋} 是不可以的。
            InputStream cssIs = new ByteArrayInputStream("* {font-family: PingFang-SC-Medium.otf;}".getBytes("UTF-8"));
//            Font font = new Font(bf, 13, Font.NORMAL);

            //加载字体
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider() {

                @Override
                public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {

                    //你的字体文件的位置
                    //这里把所有字体都设置为宋体了,可以根据fontname的值设置字体
                    String yaHeiFontName = new ClassPathResource("/Microsoft-YaHei.ttf").getPath();
                    //如果是ttc需要这一行,ttf不需要
//                    yaHeiFontName += ",1";
//                    com.lowagie.text.Font yaHeiFont;
                    Font font = null;
                    try {
                        font = new Font(com.itextpdf.text.pdf.BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                        font.setStyle(style);
                        font.setColor(color);
                        if (size > 0) {
                            font.setSize(size);
                        }
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return font;
                }
            };

            //第四个参数是html中的css文件的输入流
            //第五个参数是字体提供者，使用系统默认支持的字体时，可以不传。
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(parsedHtml.getBytes()), cssIs, fontProvider);

            // 5.关闭文档
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String html2PdfByte(String htmlStr) {

        byte[] result=null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Rectangle rectPageSize = new Rectangle(PageSize.A4);
        Document document = new Document(rectPageSize);
        try {
            BaseFont bf = BaseFont.createFont(new ClassPathResource("/Microsoft-YaHei.ttf").getPath(),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // 中文字体
            Font fontChinese = new Font(bf, 11, Font.NORMAL);

            //字体大小
            fontChinese.setSize(11);
            // 文档输出流。new FileOutputStream("/Users/apple/Downloads/111.pdf")
//            PdfWriter pdfWriter=PdfWriter.getInstance(document, new FileOutputStream("/Users/apple/Downloads/111.pdf"));
            PdfWriter writer=PdfWriter.getInstance(document, baos);
            document.open();

            //要解析的html
            //html转换成普通文字,方法如下:
            org.jsoup.nodes.Document contentDoc = Jsoup.parseBodyFragment(htmlStr);
            org.jsoup.nodes.Document.OutputSettings outputSettings = new org.jsoup.nodes.Document.OutputSettings();
            outputSettings.syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
            contentDoc.outputSettings(outputSettings);
            String parsedHtml = contentDoc.outerHtml();

            //这儿的font-family不支持汉字，{font-family:仿宋} 是不可以的。
            InputStream cssIs = new ByteArrayInputStream("* {font-family: PingFang-SC-Medium.otf;}".getBytes("UTF-8"));
//            Font font = new Font(bf, 13, Font.NORMAL);

            //加载字体
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider() {

                @Override
                public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {

                    //你的字体文件的位置
                    //这里把所有字体都设置为宋体了,可以根据fontname的值设置字体
                    String yaHeiFontName = new ClassPathResource("/Microsoft-YaHei.ttf").getPath();
                    //如果是ttc需要这一行,ttf不需要
//                    yaHeiFontName += ",1";
//                    com.lowagie.text.Font yaHeiFont;
                    Font font = null;
                    try {
                        font = new Font(com.itextpdf.text.pdf.BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                        font.setStyle(style);
                        font.setColor(color);
                        if (size > 0) {
                            font.setSize(size);
                        }
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return font;
                }
            };

            //第四个参数是html中的css文件的输入流
            //第五个参数是字体提供者，使用系统默认支持的字体时，可以不传。
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(parsedHtml.getBytes()), cssIs, fontProvider);

            document.close();
            writer.flush();

            result =baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        result = Base64Encoder.encode(result,false);

        return new String(result);

    }

    //设置页码
    private static void setFooter(PdfWriter writer,BaseFont bf) throws DocumentException, IOException{
        PDFBuilder headerFooter = new PDFBuilder(bf,8,PageSize.A4);
        writer.setPageEvent(headerFooter);
    }

    public static void main(String[] args) {
        Map<String, Object> simpleFormFields = new HashMap<>();
        simpleFormFields.put("orderNumber", "111111");
        simpleFormFields.put("deliveryType", "快递配送");
        simpleFormFields.put("name", "name");
        simpleFormFields.put("goodsName", "goodsName");
        simpleFormFields.put("contractNumber", "111111");
        simpleFormFields.put("sn", "4342424");
        simpleFormFields.put("date", DateUtil.formatDate(DateUtil.date()));
        createPDF("https://img.zejihui.com.cn/sf_ht/003.pdf", "/Users/zqdfound/Desktop/工作/test/newPDF.pdf", simpleFormFields);


    }

}
