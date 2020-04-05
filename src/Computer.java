
public class Computer {
	public static void main(String[] args) {
		int i = 1;
		i=i++;
		int j = i++;
		
		System.out.println("i=" + i);
		System.out.println("j=" + j);
	    int k = i + ++i * i++;
		System.out.println("i=" + i);
		System.out.println("j=" + j);
		System.out.println("k=" + k);
	}


}
