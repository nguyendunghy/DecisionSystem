/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TOPSIS_AHP_ELECTRE;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author NguyenVanDung
 */
public class Electre {
    
    private int row;   //số dòng của bảng
    private int column;// số cột của bản
    private float[] W;//Bộ trọng số
    private float[] table;//bảng dữ liệu
    private float[][] C; //Mảng hai chiều lưu giữ giá trị của Cpq.Với  p = q ta để là -1
    private float[][] D;// Mảng hai chiều lưu giữ giá trị của Dpq với  p = q ta để là -1
    ArrayList<Integer> result = new ArrayList<>();
    
    public Electre(int row, int column, float[] table, float[] W) {
        
        if (table.length < row * column) {
            System.out.println("Nhap lai bang du lieu");
        } else if (W.length < column) {
            System.out.println("Nhap lai bang trong so");
        } else {
            this.table = new float[row * column];
            this.W = new float[column];
            this.row = row;
            this.column = column;
            this.table = Arrays.copyOfRange(table, 0, row * column);
            this.W = Arrays.copyOfRange(W, 0, column);
            
        }
    }
    
    public ArrayList<Integer> getResult() {
        MakeCD();
        makeKeyDecision();
        return result;
    }
    
//    public static void main(String[] args) {
//        float[] input
//                = {(float) 0.0693, (float) 0.6388, (float) 0.0424,
//                    (float) 0.0717, (float) 0.2740, (float) 0.0566, 
//                    (float) 0.1733, (float) 0.0825, (float) 0.0707};
//        float[] w = {(float) 0.2, (float) 0.7, (float) 0.1};
//        Electre obj = new Electre(3, 3, input, w);
//        ArrayList<Integer> rs = obj.getResult();
//        for (Integer ele : rs) {
//            System.out.println(ele + " ");
//        }
//    }
    
    private void MakeCD() {
        C = new float[row][row];
        D = new float[row][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                //Nếu i = j thì ta gán cho cả C và D bằng -1
                if (i == j) {
                    C[i][j] = -1;
                    D[i][j] = -1;
                    continue;
                } else {
                    ArrayList<Integer> listC = new ArrayList();//Lưu trữ các giá trị cột trong tính toán C 
                    ArrayList<Integer> listD = new ArrayList();//Lưu trữ các giá trị cột trong tính toán  D
                    for (int k = 0; k < column; k++) {
                        if (table[i * column + k] >= table[j * column + k]) {
                            listC.add(k);
                        } else {
                            listD.add(k);
                        }
                        
                    }
                    //Tính giá trị cho C[i][j]
                    for (Integer t : listC) {
                        C[i][j] += W[t];
                    }
                    //Tính giá trị cho D[i][j]
                    float mother = 0; //Tính mẫu theo công thức
                    float child = 0; //Tính tử  theo công thức
                    for (int t = 0; t < column; t++) {
                        mother += Math.abs(table[i * column + t] - table[j * column + t]);
                    }
                    
                    for (Integer t : listD) {
                        child += Math.abs(table[i * column + t] - table[j * column + t]);
                    }
                    
                    if (mother == 0) {
                        System.out.println("Hai dong " + i + " va " + j + "giong y het nhau");
                    } else {
                        D[i][j] = child / mother;
                    }
                    
                }
            }
            
        }
    }
    
    private void makeKeyDecision() {
        float Ctb = 0; //Trung binh cua C
        float Dtb = 0; //Trung cua D
        boolean[] tb = new boolean[row]; //Mảng chứa các phần hàng được chọn vào key list,true là được chọn,false là loại
        for (int i = 0; i < row; i++) {
            tb[i] = true;
        }
//Chú ý tại các ô có chỉ số i=j tại mảng C và D có giá trị -1,ta phải bỏ đi row ô như thế này
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (i != j) {
                    Ctb += C[i][j];
                }
            }
        }
        Ctb = Ctb / (row * (row - 1));
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (i != j) {
                    Dtb += D[i][j];
                }
            }
        }
        Dtb = Dtb / (row * (row - 1));
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                if (i != j) {
                    if (tb[j]) {
                        if (C[i][j] >= Ctb) {
                            if (Dtb > D[i][j]) {
                                tb[j] = false;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < row; i++) {
            if (tb[i]) {
                result.add(i);
            }
        }
    }
}
