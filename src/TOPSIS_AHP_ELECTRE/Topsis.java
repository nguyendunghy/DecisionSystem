/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TOPSIS_AHP_ELECTRE;

import java.util.Arrays;

/**
 *
 * @author NguyenVanDung
 * @version 1 Đầu vào là một mảng hai chiều bảng đữ liệu đã nhân trong số Đầu ra
 * trả về dòng có giá trị sau khi xử lí tốt nhất và thứ tự các đáp án theo thứ
 * tự giảm dần
 *
 */
public class Topsis {

    private int row;   //số dòng của bảng
    private int column;// số cột của bản
    private int[] resultRow;//Hàng có giá trị tốt nhất sau khi xử lí
    private float[] table;//bảng dữ liệu
    private float[] Astar;//Mảng chứa giá trị giá trị tốt nhất
    private float[] Aminus;//Mảng chứa giá trị tồi nhất
    private float[] Sstar;//Mảng chứa giá trị khoảng cách đến các giá trị tốt nhất của các dòng
    private float[] Sminus;//Mảng chứa giá trị khoảng cách đến các giá trị nhỏ nhất của các dòng

    public Topsis(int row, int column, float[] table) {
        if (table.length < row * column) {
            System.out.println("Nhap lai bang du lieu");
        } else {
            this.table = new float[row * column];
            this.row = row;
            this.column = column;
            this.table = Arrays.copyOfRange(table, 0, row * column);

        }
    }

    public static void main(String[] args) {
        float[] input = new float[]{(float) 0.1314, (float) 0.0711, (float) 0.0925, (float) 0.0564, (float) 0.0346, (float) 0.1124, (float) 0.0894, (float) 0.0719, (float) 0.0484, (float) 0.0865, (float) 0.1143, (float) 0.0826, (float) 0.0822, (float) 0.0645, (float) 0.0605, (float) 0.1181, (float) 0.0871, (float) 0.0719, (float) 0.0806, (float) 0.0519, (float) 0.1333, (float) 0.0642, (float) 0.1027, (float) 0.0323, (float) 0.0519, (float) 0.1238, (float) 0.0917, (float) 0.0616, (float) 0.0726, (float) 0.0692};
        Topsis top = new Topsis(6, 5, input);
        // System.out.println(String.valueOf(top.getChosenRow()));

        int[] rs = top.getResultRow();
        for (int i = 0; i < top.row; i++) {
            System.out.print(rs[i] + " ");
        }
    }

    //Tính các khoảng cách từ các dòng đến các giá trị lí tưởng
    private void calculateDistance() {
        //Tính các giá trị lí tưởng
        //Lí tưởng tốt
        Astar = new float[column];  //Khởi tạo mảng chứa vector lí tưởng
        //Tìm max các giá trị trên từng cột 
        for (int j = 0; j < column; j++) {
            int max = j;
            for (int i = 0; i < row; i++) {
                if (table[i * column + j] > table[max]) {
                    max = i * column + j;
                }
            }
            Astar[j] = table[max];
        }
        //Lí tưởng tồi
        Aminus = new float[column];  //Khởi tạo mảng chứa vector lí tưởng
        //Tìm min các giá trị trên từng cột 
        for (int j = 0; j < column; j++) {
            int min = j;
            for (int i = 0; i < row; i++) {
                if (table[i * column + j] < table[min]) {
                    min = i * column + j;
                }
            }
            Aminus[j] = table[min];
        }

        //Tính khoảng cách từ các dòng đến vector lí tưởng tốt       
        Sstar = new float[row];
        //Tính khoảng cách giữa các dòng đến vector lí tưởng rồi gán vào mảng Sstar
        for (int i = 0; i < row; i++) {
            float square = 0;
            for (int j = 0; j < column; j++) {
                float tmp = (table[i * column + j] - Astar[j]);
                square += tmp * tmp; //Tính tổng các bình phương của hiệu
            }
            Sstar[i] = (float) Math.sqrt(square);
        }

        //Tính khoảng cách từ các dòng đến vector lí tưởng tồi       
        Sminus = new float[row];
        //Tính khoảng cách giữa các dòng đến vector lí tưởng rồi gán vào mảng Sminus
        for (int i = 0; i < row; i++) {
            float square = 0;
            for (int j = 0; j < column; j++) {
                float tmp = (table[i * column + j] - Aminus[j]);
                square += tmp * tmp; //Tính tổng các bình phương của hiệu
            }
            Sminus[i] = (float) Math.sqrt(square);
        }
    }

    //Tính kết quả cuối cùng đưa ra dòng có giá trị tốt nhất
    //Đây chỉ là một hàm,mình có thể mở rộng ra ra nhiều hàm khác
    private void GetResult() {
        float C[] = new float[row];
        float orderedValueOfC[] = new float[row];
        //Tính vector C
        for (int i = 0; i < row; i++) {
            C[i] = Sminus[i] / (Sminus[i] + Sstar[i]);
        }
        //Sao chép giá trị của C sang orderedValueOfC
        orderedValueOfC = Arrays.copyOfRange(C, 0, row);

        //Sắp xếp các giá trị của các lựa chọn theo thứ tự giảm dần
        //Mảng resultRow lúc đầu chứa giá trị bằng chính thứ tự của các phần tử
        resultRow = new int[row];
        for (int i = 0; i < row; i++) {
            resultRow[i] = i;

        }

        for (int i = 0; i < row; i++) {
            int max = i;
            for (int j = i + 1; j < row; j++) {
                if (orderedValueOfC[j] > orderedValueOfC[max]) {
                    max = j;
                }
            }
            //Đổi giá trị của orderedValueOfC[i] và orderedValueOfC[max]
            float temp = orderedValueOfC[max];
            orderedValueOfC[max] = orderedValueOfC[i];
            orderedValueOfC[i] = temp;

            //Lấy vị trí của lựa chọn tốt tương ứng
            int tt = resultRow[max];
            resultRow[max] = resultRow[i];
            resultRow[i] = tt;

        }

    }

    //Hàm trả về dòng có giá trị tốt nhất
    public int getChosenRow() {
        calculateDistance();
        GetResult();
        return resultRow[0];
    }

    //Hàm trả về thứ tự các lựa chọn
    public int[] getResultRow() {
        calculateDistance();
        GetResult();
        return resultRow;
    }

}
