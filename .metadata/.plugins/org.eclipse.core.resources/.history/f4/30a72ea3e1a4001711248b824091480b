import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("내 돈......");
		System.out.println("어디갔어...");
		System.out.println("어디갔어...");
		System.out.println("어디갔어...");
		System.out.println("어디갔어...");

		System.out.println("Execution time : " + Util.getCurrentDateTime());
		System.out.println("Made by Kim");

		System.out.println("오늘 사용한 돈을 입력하세요.");
		Scanner sc = new Scanner(System.in);
		int userInput = sc.nextInt();

		System.out.println("입력값 　：" + userInput);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));

			String s = "출력 파일에 저장될 이런 저런 문자열입니다.";

			out.write(s);
			out.newLine();

			out.close();

		} catch (Exception e) {
			System.out.println("error");
		}

		sc.close();

	}
}
