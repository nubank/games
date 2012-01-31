package net.vidageek.games.regex;

import java.util.ArrayList;
import java.util.List;

import net.vidageek.games.Game;
import net.vidageek.games.regex.task.PerfectMatchRegex;
import net.vidageek.games.task.Task;

final public class RegexGame implements Game {

	private final List<Task> list;

	public RegexGame() {
		int indexes = 0;
		list = new ArrayList<Task>();
		list.add(new PerfectMatchRegex(indexes++, "a"));
		list.add(new PerfectMatchRegex(indexes++, "b"));
		list.add(new PerfectMatchRegex(indexes++, "ab"));
		list.add(new PerfectMatchRegex(indexes++, "abc"));
		list.add(new PerfectMatchRegex(indexes++, "\\"));
		list.add(new PerfectMatchRegex(indexes++, "$"));
		list.add(new PerfectMatchRegex(indexes++, "abcdefg"));
		list.add(new PerfectMatchRegex(indexes++, "ab$cd^ef\\g"));
	}

	public Task task(final int index) {
		return list.get(index);
	}

	public int size() {
		return list.size();
	}

	public String getDescription() {
		return "Um jogo muito legal para aprender RegEx";
	}

	public List<Task> getTasks() {
		return list;
	}

	public String getName() {
		return "RegEx";
	}

}
