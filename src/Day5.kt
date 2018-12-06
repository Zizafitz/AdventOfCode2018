import java.io.BufferedReader
import java.io.File
import kotlin.test.assertEquals

/**
--- Day 5: Alchemical Reduction ---
You've managed to sneak in to the prototype suit manufacturing lab. The Elves are making decent progress, but are still
struggling with the suit's size reduction capabilities.

While the very latest in 1518 alchemical technology might have solved their problem eventually, you can do better. You
scan the chemical composition of the suit's material and discover that it is formed by extremely long polymers (one of
which is available as your puzzle input).

The polymer is formed by smaller units which, when triggered, react with each other such that two adjacent units of the
same type and opposite polarity are destroyed. Units' types are represented by letters; units' polarity is represented
by capitalization. For instance, r and R are units with the same type but opposite polarity, whereas r and s are
entirely different types and do not react.

For example:

In aA, a and A react, leaving nothing behind.
In abBA, bB destroys itself, leaving aA. As above, this then destroys itself, leaving nothing.
In abAB, no two adjacent units are of the same type, and so nothing happens.
In aabAAB, even though aa and AA are of the same type, their polarities match, and so nothing happens.
Now, consider a larger example, dabAcCaCBAcCcaDA:

dabAcCaCBAcCcaDA  The first 'cC' is removed.
dabAaCBAcCcaDA    This creates 'Aa', which is removed.
dabCBAcCcaDA      Either 'cC' or 'Cc' are removed (the result is the same).
dabCBAcaDA        No further actions can be taken.
After all possible reactions, the resulting polymer contains 10 units.

How many units remain after fully reacting the polymer you scanned? (Note: in this puzzle and others, the input is
large; if you copy/paste your input, make sure you get the whole thing.)

--- Part Two ---
Time to improve the polymer.

One of the unit types is causing problems; it's preventing the polymer from collapsing as much as it should. Your goal
is to figure out which unit type is causing the most problems, remove all instances of it (regardless of polarity),
fully react the remaining polymer, and measure its length.

For example, again using the polymer dabAcCaCBAcCcaDA from above:

Removing all A/a units produces dbcCCBcCcD. Fully reacting this polymer produces dbCBcD, which has length 6.
Removing all B/b units produces daAcCaCAcCcaDA. Fully reacting this polymer produces daCAcaDA, which has length 8.
Removing all C/c units produces dabAaBAaDA. Fully reacting this polymer produces daDA, which has length 4.
Removing all D/d units produces abAcCaCBAcCcaA. Fully reacting this polymer produces abCBAc, which has length 6.
In this example, removing all C/c units was best, producing the answer 4.

What is the length of the shortest polymer you can produce by removing all units of exactly one type and fully reacting
the result?
 */
class Day5(): Day() {
	infix fun Any.`should equal`(expected: Any) = assertEquals(expected, this)

	private fun removePolymers(charArr: CharArray): CharArray {
		var charA = charArr
		var modified = true
		while (modified) {
			modified = false
			for (i in 0 until charA.lastIndex) {
				if (((charA[i].isUpperCase() && !charA[i+1].isUpperCase())
								|| (!charA[i].isUpperCase() && charA[i+1].isUpperCase()))
						&& charA[i].toUpperCase().equals(charA[i+1].toUpperCase())) {
					modified = true
					charA[i] = '#'
					charA[i+1] = '#'
				}
			}
			charA = String(charA).replace("#", "").toCharArray()
		}

		return charA
	}
	private fun subQuestion1(lineList: MutableList<String>): Int {
		val charA = removePolymers(lineList.first().toCharArray())
		return charA.size
	}

	private fun subQuestion2(lineList: MutableList<String>): Int {
		val charA = lineList.first().toCharArray()
		var size = charA.size
		for (c in 'a'..'z') {
			val cSize = removePolymers(String(charA).replace(c.toString(), "", true)
					.toCharArray()).size
			if (cSize < size) {
				size = cSize
			}
		}
		return size
	}

	override fun show() {
		println("5 December:")

		val bufferedReader: BufferedReader = File("input/Day5.txt").bufferedReader()
		val lineList = mutableListOf<String>()
		bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }

		subQuestion1(mutableListOf("aA")) `should equal` (0)
		subQuestion1(mutableListOf("abBA")) `should equal` (0)
		subQuestion1(mutableListOf("abAB")) `should equal` (4)
		subQuestion1(mutableListOf("aabAAB")) `should equal` (6)
		subQuestion1(mutableListOf("dabAcCaCBAcCcaDA")) `should equal` (10)
		val s1 = System.currentTimeMillis()
		print("Answer part 1: " + subQuestion1(lineList))
		println("\t" + (System.currentTimeMillis() - s1) + " ms")

		subQuestion2(mutableListOf("dabAcCaCBAcCcaDA")) `should equal` (4)
		val s2 = System.currentTimeMillis()
		print("Answer part 2: " + subQuestion2(lineList))
		println("\t" + (System.currentTimeMillis() - s2) + " ms")
		println()

	}
}