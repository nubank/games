package net.vidageek.games.regex.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.vidageek.games.regex.task.CaptureGroup;
import net.vidageek.games.task.JudgedTask;
import net.vidageek.games.task.status.Error;
import net.vidageek.games.task.status.Failed;

import org.junit.Test;

final public class CaptureGroupTest {

	@Test
	public void shouldCaptureASingleGroup() {
		JudgedTask judge = new CaptureGroup("abcdef", "abcdef").judge("([a-z]+)");
		assertTrue(judge.ok());
	}

	@Test
	public void shouldMatchGroupOneTarget() {
		JudgedTask judge = new CaptureGroup("abcdef1a", "abcdef").judge("([a-z]+).*");
		assertTrue(judge.ok());
	}

	@Test
	public void shouldMatchAllGroupsTarget() {
		JudgedTask judge = new CaptureGroup("abcdef1a", "abcdef", "1a").judge("([a-z]+)(.*)");
		assertTrue(judge.ok());
	}

	@Test
	public void shouldContainACaptureGroup() {
		JudgedTask judge = new CaptureGroup("abcdef", "abcdef").judge("[a-z]+");
		assertFalse(judge.ok());
		assertEquals(Failed.class, judge.getClass());
	}

	@Test
	public void shouldFailIfDoesNotMatch() {
		JudgedTask judge = new CaptureGroup("abcdef", "abcdef").judge("(\\d+)");
		assertFalse(judge.ok());
		assertEquals(Failed.class, judge.getClass());
	}

	@Test
	public void shouldFailIfGroup0IsNotTheMatchingTarget() {
		JudgedTask judge = new CaptureGroup("abcdef", "abcdef").judge("([a-d]+)");
		assertFalse(judge.ok());
		assertEquals(Failed.class, judge.getClass());
	}

	@Test
	public void shouldFailIfGroup1IsNotTheMatchingTarget() {
		JudgedTask judge = new CaptureGroup("abcdef", "abcdef").judge("([a-d]+)ef");
		assertFalse(judge.ok());
		assertEquals(Failed.class, judge.getClass());
	}

	@Test
	public void shouldReturnErrorIfRegexIsNotParseable() {
		JudgedTask judge = new CaptureGroup("abcdef", "abcdef").judge("([a-d]+ef");
		assertFalse(judge.ok());
		assertEquals(Error.class, judge.getClass());
	}

}
