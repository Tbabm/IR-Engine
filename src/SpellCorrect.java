import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class SpellCorrect {
	
	private final static String fileName = "Raw_Stemmed_Dic";
	
	public static String spellCorrect(String word){
		String lowerword = word.toLowerCase();
		if(isCorrect(lowerword)){
			return word;
		}
		return correct(lowerword);
	}

	
	//���
	private static boolean isCorrect(String word){
		 if(IR_system.rawStemTree.get(word) != null)
			 return true;
		 return false;
	}
	
	//����
	private static String correct(String word) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String recommendStr = word;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            int maxDis = word.length();
            while ((tempString = reader.readLine()) != null) {
            	String[] result = tempString.split("\t");
            	if(getEditDis(result[0],word) < maxDis){
            		maxDis = getEditDis(result[0],word);
            		recommendStr = result[0];
            		//System.out.println(recommendStr);
            		//System.out.println(maxDis);
            		//System.out.println(getEditDis(result[0],word));
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return recommendStr;
    }
	
	private static int getEditDis(String str1, String str2)
	{
		int[][] f = new int[str1.length()+1][str2.length()+1];
		for(int i = 0;i <= str1.length();++i)
			Arrays.fill(f[i], 0x7fffffff);
		f[0][0] = 0;
		for(int i = 0;i <= str1.length();++i)
			for(int j = 0;j <= str2.length();++j)
			{
				if(i!=0) {
					if(f[i][j] > f[i-1][j]+1)
						f[i][j] = f[i-1][j]+1;
				}
				if(j!=0){
					if(f[i][j] > f[i][j-1]+1)
						f[i][j] = f[i][j-1]+1;
				}
				if(i!=0&&j!=0){
					if(str1.charAt(i-1)==str2.charAt(j-1)){
						if(f[i][j] > f[i-1][j-1])
							f[i][j] = f[i-1][j-1];
					}
					else{
						if(f[i][j] > f[i-1][j-1]+1)
							f[i][j] = f[i-1][j-1]+1;
					}
				}
			}
		return f[str1.length()][str2.length()];
	}
}
