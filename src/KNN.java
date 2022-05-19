import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

class Knn {


    public static void main(String[] args){
        try{
            //读取训练集
            List<int[]> trainVects = new ArrayList<>();		//训练集特征向量
            List<Integer> trainLabels = new ArrayList<>();	//训练集Label
            readDataSet("D:\\Java\\KNN\\train.csv", trainVects, trainLabels);
            //读取测试集
            List<int[]> testVects = new ArrayList<>();		//测试集特征向量
            List<Integer> testLabels = new ArrayList<>();		//测试集Label
            readDataSet("D:\\Java\\KNN\\test_200.csv", testVects, testLabels);

            double s = 0;
            for(int i = 0; i < testVects.size(); i++){
                Map<Integer, Double> dis = new HashMap<Integer, Double>();
                for(int j = 0; j < trainLabels.size(); j++){
                    double distance = calDistance(testVects.get(i), trainVects.get(j));
                    dis.put(j, distance);
                }
                LinkedHashMap<Integer, Double> dis1 = sortMap(dis);
                int[] a=new int[10];
                int K = 3;
                for(Integer num : dis1.keySet()){
                    int number = Integer.parseInt(String.valueOf(num));
                    int n = trainLabels.get(number);
                    a[n]++;
                    K--;
                    if(K < 0){
                        break;
                    }
                }
                int max = a[0], maxValue = 0;
                for(int p = 1; p < 10; p++){
                    if(a[p] > max){
                        max = a[p];
                        maxValue = p;
                    }
                }
                System.out.println("分类结果： " + maxValue + " 实际结果： " + testLabels.get(i));
                if(maxValue == testLabels.get(i)){
                    s++;
                }
                dis.clear();
                dis1.clear();
            }
            System.out.println("优化后的分类正确率：" + (s/200.0) * 100 + "%");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //读取特征向量和label
    private static void readDataSet(String path, List<int[]> vects, List<Integer> labels) throws Exception{
        //数据集文件名
        File trainFile = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(trainFile));
        String line = "";
        br.readLine();  //忽略表头
        //遍历所有行
        while ((line = br.readLine()) != null) {   //使用readLine方法，一次读一行
            String[] items = line.split(",");
            int len = items.length;
            int[] vect = new int[len - 1];
            //读取第一列，写入label
            labels.add(Integer.parseInt(items[0]));
            //遍历后续列，写入特征向量
            for(int i = 1; i < len; i ++){
                vect[i - 1] = Integer.parseInt(items[i]);
            }
            vects.add(vect);
        }
        br.close();
    }


    //定义一个算法来计算每两个向量之间的距离
    public static double calDistance(int[] a, int[] b){
        double result = 0.0;
        int temp = 0;
        for(int i = 0; i < a.length; i++){
            temp += (a[i] - b[i])*(a[i] - b[i]);
        }
        result = Math.sqrt(temp);

        return result;
    }

    //计算测试集向量点与训练集向量点的距离后，按距离从小到大进行排序
    public static LinkedHashMap<Integer, Double> sortMap(Map<Integer, Double> map) {
        LinkedHashMap<Integer, Double> linkedHashMap = new LinkedHashMap<>();
        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                //升序排序
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry<Integer, Double> entry : list) {
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }


}