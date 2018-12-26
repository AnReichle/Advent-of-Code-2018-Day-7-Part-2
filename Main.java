import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		String path = "src\\input00.txt";
		ArrayList<String> input = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String str;
			while((str = br.readLine()) != null) {
				input.add(str);
			}
		}
		Character c;
		HashMap<Character, ArrayList<Character>> constraints = new HashMap<Character, ArrayList<Character>>();
		for(String str : input) {
			if(!constraints.keySet().contains(c = str.charAt(36))) {
				constraints.put(c, new ArrayList<Character>());
				constraints.get(c).add(str.charAt(5));
			} else {
				constraints.get(c).add(str.charAt(5));
			}
		}
		ArrayList<Character> finished = new ArrayList<Character>();
		ArrayList<Character> waiting = new ArrayList<Character>();
		for(c = 'A'; c <= 'Z'; c++) waiting.add(c);
		Collections.sort(waiting, Character::compare);
		HashMap<Integer, List<Character>> schedule = new HashMap<Integer, List<Character>>();
		int time = 0;
		Character aux;
		while(waiting.size()>0) {
			if(numbTasks(schedule) < 6 && (aux = nextElement(finished, waiting, constraints)) != null) {
				Character chara = nextElement(finished, waiting, constraints);
				waiting.remove(chara);
				addToSchedule(schedule, chara, time);
			} else {
				time = schedule.keySet().stream().min(Integer::compare).get();
				for(Character ch : schedule.get(time)) {
					waiting.remove(ch);
					finished.add(ch);
				}
				schedule.remove(time);
				if((aux = nextElement(finished, waiting, constraints)) != null) {
					Character chara = nextElement(finished, waiting, constraints);
					waiting.remove(chara);
					addToSchedule(schedule, chara, time);
				}			
			}
		}
		time = schedule.keySet().stream().max(Integer::compare).get();
		finished.stream().forEach(System.out::print);
		System.out.println("\n" + time);
	}
	static Character nextElement(ArrayList<Character> finished, ArrayList<Character> waiting, HashMap<Character, ArrayList<Character>> constraints) {
		Character c = null;
		for(Character ch : waiting) {
			if(!constraints.keySet().contains(ch) || aSubsetTOB(constraints.get(ch), finished)) {
				c = ch;
				break;
			}
		}
		return c;
	}
	static boolean aSubsetTOB(ArrayList<Character> constraintSet, ArrayList<Character> finished) {
		for(Character ch : constraintSet) {
			if(!finished.contains(ch)) return false;
		}
		return true;
	}
	static int numbTasks(HashMap<Integer, List<Character>> schedule) {
		return schedule.values().stream().mapToInt(e -> e.size()).sum();
	}
	static void addToSchedule(HashMap<Integer, List<Character>> schedule, Character ch, int time) {
		int finTime = time + 60 + ch - 64;
		if(!schedule.keySet().contains(finTime)) {
			schedule.put(finTime, new ArrayList<Character>());
		}
		schedule.get(finTime).add(ch);	
	}
}
