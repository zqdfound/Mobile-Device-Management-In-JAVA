package com.easyback.framework.utils;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuangqingdian
 * @date 2019/07/29
 */
public class FindKeywordsUtil {

    // 指定关键字
    public static String KEY_WORD = "出租方（盖章";
    // PDF当前页数
    public static int curPage = 0;
    // 集合里放了多少条数据
    public static int count = 0;
    // 坐标信息集合
    public static List<float[]> arrays = new ArrayList<float[]>();

    /**
     * 获取指定PDF文件中指定关键字的坐标
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<float[]> getKeyWordCoordinate(String filePath) throws Exception {

        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(filePath);
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
                        if (text.contains(KEY_WORD) && text != null) {
                            // 把当前从PDF文件中读取的文字横坐标、纵坐标和当前页存到集合里面
                            Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                            float[] resu = new float[3];
                            resu[0] = boundingRectange.x;
                            resu[1] = boundingRectange.y;
                            resu[2] = curPage;
                            arrays.add(resu);
                            count++;

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
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
        }

        return arrays;
    }

}
