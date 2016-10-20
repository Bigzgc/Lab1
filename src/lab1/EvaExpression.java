package lab1;

import java.util.Arrays;
import java.util.Scanner;   
import java.util.regex.Pattern;   

public class EvaExpression {
	//主程序，表达式的输入
    public static void main(String args[]){
    	String Nstr = "";
    	System.out.println("输入'Exit'退出程序！");
    	Scanner n = new Scanner(System.in);
    	ProExpression exp = new ProExpression();
    	long starTime=System.currentTimeMillis();//测试程序运行时间
    	while(true){
    		Nstr = n.nextLine();//输入
    		if("Exit".equals(Nstr)){
    			break;
    		}
    		exp.ProExp(Nstr);
    	}
		long endTime=System.currentTimeMillis();//
		long Time=endTime-starTime;//
		System.out.println("程序执行时间："+Time+"毫秒");//快来
    	n.close();
    }
}

class ProExpression{
	String MemStr = "";
	String DerStr = "";
	String Estr = "";
	
	//整理表达式
	private String StaExp(String str){
		String SEstr;
		SEstr = str;
		int j = 0;
	    while(j!=-1){//把x^2转换为x*x等
	    	j = SEstr.indexOf('^');
	    	if(j != -1){
	    	    String str0 = SEstr.substring(j-1,j);
	    	    String str1 = "";
	    	    int i;
	    	    for(i=j+1;i<SEstr.length();i++){
	    	    	if(!(SEstr.charAt(i)>=48 && SEstr.charAt(i)<=57)){
	    	    		break;
	    	    	}
	    	    }
	    	    String str2="";
	    	    str2 = SEstr.substring(j+1,i);
	    	    int h = Integer.valueOf(str2).intValue();
	    	    for(int k=0;k<h;k++){
	    	    	str1 = str1 + str0 + "*";
	    	    }
	    	    SEstr = SEstr.substring(0, j-1)+
	    	    		str1.substring(0,str1.length()-1)+SEstr.substring(i);
	    	}
		}
	    String Rstr="";//把系数提出来，变量按序排列
		String[] SEstrArray = SEstr.split("\\+");
		String[][] Array = new String[SEstrArray.length][2];
		for(int k=0;k<SEstrArray.length;k++){
			String[] _SEstrArray = SEstrArray[k].split("\\*");
			float num=1;
			String str0="";
			String[] str1 = new String[_SEstrArray.length];
			int m = 0;
			for(int l=0;l<_SEstrArray.length;l++){
				char ch = _SEstrArray[l].charAt(0);
				if((ch>='0' && ch<='9')||ch=='-'){
					num=num*Float.valueOf(_SEstrArray[l]).floatValue();
				}
				else{
					str1[m++]=_SEstrArray[l];
				}
			}
			Array[k][0]=String.valueOf(num);
			if(m>0){
				String[] str2 = new String[m];
				for(int n=0;n<m;n++){
					str2[n] = str1[n];
				}
				Arrays.sort(str2);
				for(int n=0;n<m;n++){
					str0+=str2[n]+"*";
				}
				Array[k][1]=str0.substring(0,str0.length()-1);
			}
			else{
				Array[k][1]="";
			}
		}
		for(int k=0;k<SEstrArray.length-1;k++){
			for(int l=k+1;l<SEstrArray.length;l++){
				if(Array[k][1].equals(Array[l][1])){
					Array[k][0]=String.valueOf(
							    Float.valueOf(Array[k][0]).floatValue()+
							    Float.valueOf(Array[l][0]).floatValue());
					Array[l][1]="#";
				}
			}
		}
		for(int k=0;k<SEstrArray.length;k++){
			if(Array[k][1]!="#"){
				if(Array[k][1]!=""){
				    Rstr += Array[k][0]+"*"+Array[k][1]+"+";
				}
				else{
					Rstr += Array[k][0]+"+";
				}
			}
		}
		return Rstr.substring(0, Rstr.length()-1);
	}
	//判断表达式是否正确
	private boolean expression(String str){
		str = str.replace(" ", "");
		str = str.replace("	", "");
		boolean result;
		int flag = 0;
		int i = 0;
		char ch0, ch1, ch2;
		if(str.length()==0){
			flag=1;
		}
		else if(str.length()==1){//单字符
			char ch = str.charAt(0);
			if(!((ch>=48 && ch<=57)||
			   (ch>=65 && ch<=90)||(ch>=97 && ch<=122))){
				flag = 1;
			}
		}
		else{
			for(i=0; i<str.length()-1; i++){
				ch0 = str.charAt(i);
				ch1 = str.charAt(i+1);
				if(ch0==42 || ch0==43){//*+
					if(i==0 || (!((ch1>=48 && ch1 <= 57) || 
					   (ch1>=65 && ch1<=90)||(ch1>=97 && ch1<=122)))){
						flag = 1;break;
					}
				}
				else if(ch0==45){//-
					if(i==0 || (!((ch1>=48 && ch1 <= 57) || 
					   (ch1>=65 && ch1<=90)||(ch1>=97 && ch1<=122)))){
					    flag = 1;break;
				    }
					else{
						str = str.substring(0,i)+"+"+str.substring(i);
						i+=1;
					}
				}
				else if(ch0==94){//^
					if(i==0){
						flag=1;break;
					}
					else{
						char ch_1 = str.charAt(i-1);
						if(!(((ch_1>=65 && ch_1<=90)||
						   (ch_1>=97 && ch_1<=122)||
						   (ch_1>=48 && ch_1<=57))&&
						   (ch1>=48 && ch1<=57))){
							flag=1;break;
						}
					}
				}
				else if(ch0>=48 && ch0<=57){//3x，300，3+/*/-/^
					if((ch1>=48 && ch1<=57)||
					   (ch1==42 || ch1==43 || ch1==45 ||ch1==94)){
						str = str.substring(0,i+1)+""+str.substring(i+1);
					}
					else if((ch1>=65 && ch1<=90)||(ch1>=97 && ch1<=122)){
						str = str.substring(0,i+1)+"*"+str.substring(i+1);
					}
					else{
						flag=1;break;
					}
				}
				else if((ch0>=65 && ch0<=90)||(ch0>=97 && ch0<=122)){//x3 xy
					if((ch1>=48 && ch1<=57)||
					   (ch1>=65 && ch1<=90)||(ch1>=97 && ch1<=122)){
						str = str.substring(0,i+1)+"*"+str.substring(i+1);
					}
					else if(ch1==42 || ch1==43 || ch1==45 ||ch1==94){
						str = str.substring(0,i+1)+""+str.substring(i+1);
					}
					else{
						flag=1;break;
					}
				}
				else{
					flag = 1;break;
				}
			}
			ch2 = str.charAt(str.length()-1);
			if(ch2==42 || ch2==43 || ch2==45 || ch2==94){
				flag = 1;
			}
		}
		if(flag == 1){
			Estr = "Expressions is not valid!";
            result = false;
		}
		else{
			Estr = str;
			Estr = StaExp(Estr);
			result = true;
		}
		return result;
	}
	//用数值替换相应的变量
	private String simplify(String str){
		int i;
		int flag=0;
		String Sstr = MemStr;
		String[] strArray = str.split(" ");
		if(strArray.length > 1){
			for(i=1;i<strArray.length;i++){
				String[] _strArray = strArray[i].split("=");
				int k = Sstr.indexOf(_strArray[0]);
				if(k!=-1){
					Sstr = Sstr.replace(_strArray[0], _strArray[1]);
				}
				else{
					System.out.println("Variable Error!");
					flag = 1;break;
				}
			}
		}
		if(flag==1){
			return "";
		}
		else{
		    return StaExp(Sstr);
		}
	}
    //求导
	private boolean derivative(String str){
		DerStr = "";
		int i;
		int cnt = 0;
		String[] strArray = str.split(" ");
		String[] CstrArray = MemStr.split("\\+");
		for(i=0;i<CstrArray.length;i++){
			String estr = "";
			int k = CstrArray[i].indexOf(strArray[1]);
			if(k!=-1){
				int cnt1 = 0;
				for(int j=0;j<CstrArray[i].length();j++){//计算变量个数，确定系数
					if(CstrArray[i].charAt(j)==strArray[1].charAt(0)){
						cnt1 +=1;
					}
				}
			    estr = CstrArray[i].substring(0, k)+CstrArray[i].substring(k+1);
			    if(CstrArray[i].charAt(k)=='*'){
			    	estr = estr.substring(0, k)+estr.substring(k+1);
			    }
			    else if(CstrArray[i].charAt(k-1)=='*'){
			    	estr = estr.substring(0, k-1)+estr.substring(k);
			    }
			    if(cnt1>1){
			    	estr = estr +"*"+String.valueOf(cnt1);
			    }
			    DerStr += estr;
			    DerStr += "+";
			}
			else{
				cnt +=1;
			}
		}
		if(cnt==CstrArray.length){
			System.out.println("Variable Error!");
			return false;
		}
		else{
			DerStr = DerStr.substring(0,DerStr.length()-1);
			DerStr = StaExp(DerStr);
			return true;
		}
	}	
	//判断输入是 简化命令/求导命令/表达式？
	public void ProExp(String str){
		boolean result1 = Pattern.matches("!simplify( [a-z | A-Z]=-{0,1}[0-9]{0,}\\.{0,1}[0-9]*)*",str); 
		boolean result2 = Pattern.matches("!d/d [a-z | A-Z]",str); 
		if(result1 == true){
			String str0 = simplify(str);
			if(!str0.equals("")){
			    System.out.println(str0);
			}
		}
		else if(result2 == true){
			if(derivative(str)){
			    System.out.println(DerStr);
			}
		}
		else{
			boolean r = expression(str);
			if(r == true){
				this.MemStr = this.Estr;
				System.out.println(MemStr);
			}
			else{
				System.out.println(Estr);
			}	
		}
	}
}

