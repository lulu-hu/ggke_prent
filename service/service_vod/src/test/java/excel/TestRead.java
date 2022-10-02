package excel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        String fileName = "D:\\guigu.xls";

        EasyExcel.read(fileName,User.class, new ExcelListener()).sheet().doRead();
    }
}
