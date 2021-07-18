package com.stu.base.enumstu;

import com.mybigdata.model.Student;

/**
 * @author: 今天风很大
 * @date:2021/7/17 23:14
 * @Description:
 */
public enum StudentEnum {
    username(1,"0","String"){
        @Override
        public void buildData(Student student) {

        }
    };
    public abstract void buildData(Student student);

    private int no;
    private String fieldname;
    private String type;
    StudentEnum(int no,String fieldname,String type){
        this.no=no;
        this.fieldname=fieldname;
        this.type=type;
    }

    public static void main(String[] args) {
        StudentEnum[] values = StudentEnum.values();
        for (int i = 0; i < values.length; i++) {
        }
    }
}
