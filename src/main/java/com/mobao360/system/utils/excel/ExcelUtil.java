package com.mobao360.system.utils.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.NetUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lijun
 * @date: 2018/11/16
 * @description: excel处理类
 */
public class ExcelUtil {

    private final static int BIG_SIZE = 10000;




    /**
     * 注解导出-针对一个sheet的方法
     * @param data 需要导出数据
     * @param clazz 类型
     * @param title 标题 不填默认文件名
     * @param fileName 文件名
     * @param <T>
     */
    public static <T> void exportExcel(List<T> data, Class<T> clazz, String title, String fileName){
        exportExcel(data,clazz,title,fileName,null);
    }


    /**
     * 注解导出-针对一个sheet的方法
     * @param data 需要导出数据
     * @param clazz 类型
     * @param title 标题 不填默认文件名
     * @param fileName 文件名
     * @param sheetName sheet名
     * @param <T>
     */
    public static <T> void exportExcel(List<T> data, Class<T> clazz, String title, String fileName, String sheetName){
        if (null == clazz) {
            throw new MobaoException("class不能为空");
        }
        title = StringUtils.isNotBlank(title) ? title : fileName;
        ExportParams params = defaulExportParams(title, sheetName);

        Workbook workbook;
        //数据大于10000用大数据导出
        if(null!=data && data.size()>ExcelUtil.BIG_SIZE){
            workbook = ExcelExportUtil.exportBigExcel(params, clazz, data);
            ExcelExportUtil.closeExportBigExcel();
        }else{
            workbook = ExcelExportUtil.exportExcel(params, clazz, data);
        }
        //导出
        defaultExport(workbook, params.getType(), fileName);
    }


    /**
     * 注解导出-针对多个sheet的方法
     * 可自定义导出列数
     * @param listSheet 导出数据以及类型和sheetName
     * @param fileName 文件名
     */
    public static <T> void exportExcel(List<ExcelSheet> listSheet, String fileName){
        if (null == listSheet || listSheet.size() <= 0) {
            throw new MobaoException("数据列表为空");
        }
        //导出
        List<Map<String, Object>> list = new ArrayList<>();
        for (ExcelSheet sheet : listSheet){
            Map<String, Object> map = new HashMap<>();
            if(null != sheet ){
                ExportParams params = defaulExportParams(sheet.getTitle(),null);
                if(StringUtils.isNotBlank(sheet.getSheetName())){
                    params.setSheetName(sheet.getSheetName());
                }
                map.put("title",params);
                map.put("entity",sheet.getClazz());
                map.put("data",sheet.getData());
                list.add(map);
            }
        }
        Workbook workbook = ExcelExportUtil.exportExcel(list,ExcelType.HSSF);
        //导出
        defaultExport(workbook,ExcelType.HSSF,fileName);
    }


    /**
     * 注解导出
     * @param workbook
     * @param fileName
     */
    private static void defaultExport(Workbook workbook, ExcelType type, String fileName){

        //重置响应对象
        resetResponse(getExcelSuffix(type), fileName);
        //导出
        HttpServletResponse response = NetUtil.getResponse();
        ServletOutputStream outStream = null;

        try {
            outStream = response.getOutputStream();
            workbook.write(outStream);
        }catch (IOException e){
            throw new MobaoException("导出文件异常");
        }finally{
            try {
                outStream.close();
            } catch (IOException e) {
                throw new MobaoException("关闭流异常");
            }
        }

    }


    /**
     * 重置响应对象
     */
    private static void resetResponse(String excelSuffix, String fileName){
        HttpServletResponse response = NetUtil.getResponse();
        //重置响应对象
        response.reset();
        String newFileName = encodeDownloadName(fileName + excelSuffix );
        response.setHeader("Content-Disposition", "attachment;filename= "+ newFileName);
        // 告诉浏览器用什么软件可以打开此文件
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }


    /**
     * 默认ExportParams参数
     * @param title
     * @return
     */
    private static ExportParams defaulExportParams(String title,String sheetName) {

        ExportParams params = new ExportParams();
        params.setStyle(ExcelStyleType.BORDER.getClazz());
        params.setTitle(title);
        if(StringUtils.isNotBlank(sheetName)){
            params.setSheetName(sheetName);
        }
        return params;
    }


    /**
     * 设置下载文件的编码，兼容大部分浏览器，不确保兼容所有
     * @param fileName 文件名
     * @return
     * @throws Exception
     */
    private static String encodeDownloadName(String fileName){
        //HTTP 请求的用户代理头的值
        String userAgent = NetUtil.getRequest().getHeader("User-Agent");

        try {
            //针对IE或者以IE为内核的浏览器：
            boolean isIE = (null != userAgent &&
                    (userAgent.toUpperCase().contains("MSIE") || userAgent.toUpperCase().contains("TRIDENT") || userAgent.toUpperCase().contains("EDGE")) );
            if (isIE) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            }

            fileName = StringUtils.deleteWhitespace(fileName);
            return fileName;
        }catch (UnsupportedEncodingException e) {
            throw new MobaoException("文件名编码失败");
        }

    }


    /**
     * 注解导出 获取后缀名
     * @return
     */
    private static String getExcelSuffix(ExcelType type) {
        if (ExcelType.XSSF.equals(type) ) {
            return ".xlsx";
        } else {
            return ".xls";
        }

    }

}
