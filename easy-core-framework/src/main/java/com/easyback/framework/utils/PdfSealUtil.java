package com.easyback.framework.utils;

import cn.hutool.core.util.ObjectUtil;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * pdf关键字定位盖章
 * @author zhuangqingdian
 * @date 2019/07/24
 */
public class PdfSealUtil {

    /**
     * pdf关键字定位盖章
     * @param fileUrl 文件路径
     * @param url 图片路径
     * @param keywords 关键字 （多个关键字用，隔开-----"打印人,打印机构"）
     * @param sealWidth 印章宽度
     * @param sealHeight 印章高度 90 45
     */
    public static void printSign(String fileUrl, String url, String keywords, float sealWidth, float sealHeight, Integer xDeviation, Integer yDeviation)  throws Exception {

        // 读取模板文件
        InputStream input = new FileInputStream(new File(fileUrl));
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileUrl));

        //查找关键字所在坐标
        List<float[]> arrayList = getKeyWordCoordinate(keywords, reader);
        //一个坐标也没找到，就返回
        if (ObjectUtil.isEmpty(arrayList)) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {

            int pageNo = (int)arrayList.get(i)[2];
            float x = arrayList.get(i)[0];
            float y = arrayList.get(i)[1];

            // 读图片
            Image image = Image.getInstance(url);
            // 获取操作的页面
            PdfContentByte under = stamper.getOverContent(pageNo);
            // 根据域的大小缩放图片
            image.scaleToFit(sealWidth, sealHeight);
            // 添加图片
            image.setAbsolutePosition(x+xDeviation, y+yDeviation);
            under.addImage(image);
        }

        input.close();
        stamper.close();
        reader.close();

    }


    // PDF当前页数
    public static int curPage = 0;

    /**
     * 获取指定PDF文件中指定关键字的坐标
     * @param keywords
     * @param pdfReader
     * @return
     * @throws Exception
     */
    public static List<float[]> getKeyWordCoordinate(String keywords, PdfReader pdfReader) {

        List<float[]> arrays = new ArrayList<float[]>();

        try {
            // 坐标信息集合
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
            for (curPage = 1; curPage <= pageNum; curPage++) {
                pdfReaderContentParser.processContent(curPage, new RenderListener() {
                    // 关键字标志
                    boolean isKeyWord = false;
                    // PDF读出来的文字与截取关键字的第index位对比，如果在关键字的长度里
                    /**
                     * 这里从PDF中是一个一个读取文字的，把读取出来的PDF文字先和关键字的第一个字比较看是否相同：
                     * 如果相同，则假设已经找到关键字（将isKeyWord设为true），把这个关键字的坐标存到集合里面，并计算集合里存放了多少个数据（count++）、设置进入到一个循环（loop
                     * = true一个循环即为关键字从第一个字到最后一个字依次和读取的PDF文字比较的过程）；
                     * 如果在这个比较过程中发现有的字符和关键字不匹配，则移除与之对应的集合里的数据。
                     */
                    // 关键字的第几位
                    int index = 0;
                    // 对比关键字是否进入到一个循环
                    boolean loop = false;

                    // 处理数据
                    @Override
                    public void renderText(TextRenderInfo textRenderInfo) {
                        String text = textRenderInfo.getText();
                        if (text != null && text.contains(keywords)) {
                            // 把当前从PDF文件中读取的文字横坐标、纵坐标和当前页存到集合里面
                            Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                            float[] resu = new float[3];
                            resu[0] = boundingRectange.x;
                            resu[1] = boundingRectange.y;
                            resu[2] = curPage;
                            arrays.add(resu);

                        }

                    }

                    @Override
                    public void renderImage(ImageRenderInfo arg0) {

                    }

                    @Override
                    public void endTextBlock() {

                    }

                    @Override
                    public void beginTextBlock() {

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrays;
    }

}
