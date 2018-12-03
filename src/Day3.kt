import java.io.BufferedReader
import java.io.File
import kotlin.test.assertEquals

/**
--- Day 3: No Matter How You Slice It ---
The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit (thanks to someone who helpfully wrote
its box IDs on the wall of the warehouse in the middle of the night). Unfortunately, anomalies are still affecting them
- nobody can even agree on how to cut the fabric.

The whole piece of fabric they're working on is a very large square - at least 1000 inches on each side.

Each Elf has made a claim about which area of fabric would be ideal for Santa's suit. All claims have an ID and consist
of a single rectangle with edges parallel to the edges of the fabric. Each claim's rectangle is defined as follows:

The number of inches between the left edge of the fabric and the left edge of the rectangle.
The number of inches between the top edge of the fabric and the top edge of the rectangle.
The width of the rectangle in inches.
The height of the rectangle in inches.
A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the left edge, 2 inches from
the top edge, 5 inches wide, and 4 inches tall. Visually, it claims the square inches of fabric represented by # (and
ignores the square inches of fabric represented by .) in the diagram below:

...........
...........
...#####...
...#####...
...#####...
...#####...
...........
...........
...........
The problem is that many of the claims overlap, causing two or more claims to cover part of the same areas. For example,
consider the following claims:

#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2
Visually, these claim the following areas:

........
...2222.
...2222.
.11XX22.
.11XX22.
.111133.
.111133.
........
The four square inches marked with X are claimed by both 1 and 2. (Claim 3, while adjacent to the others, does not
overlap either of them.)

If the Elves all proceed with their own plans, none of them will have enough fabric. How many square inches of fabric
are within two or more claims?

--- Part Two ---
Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single square inch of fabric with any
other claim. If you can somehow draw attention to it, maybe the Elves will be able to make Santa's suit after all!

For example, in the claims above, only claim 3 is intact after all claims are made.

What is the ID of the only claim that doesn't overlap?
*/
class Day3: Day {
	constructor() : super()

	infix fun Any.`should equal`(expected: Any) = assertEquals(expected, this)
	inline fun <reified T> matrix(height: Int, width: Int, initialize: () -> T) =
			Array(height) {Array(width) {initialize()}}

	var sheet = matrix(1000,1000) {0}

	private fun subQuestion1(lineList: MutableList<String>): Int {
		val regex = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()
		lineList.forEach {
			val matchResult = regex.find(it.trim())
			val x = matchResult!!.destructured.component2().toInt()
			val y = matchResult!!.destructured.component3().toInt()
			val w = matchResult!!.destructured.component4().toInt()
			val h = matchResult!!.destructured.component5().toInt()
			sheet?.let {
				for (ay in y until (y+h) ) {
					var array = arrayOf<Int>()
					for (ax in x until (x+w)) {
						sheet[ax][ay]++
					}
				}
			}
		}
		var sum = 0
		for (array in sheet) {
			for (value in array) {
				if (value > 1)
					sum++
			}
		}
		return sum
	}

	private fun subQuestion2(lineList: MutableList<String>): String {
		val regex = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()
		var res = ""
		lineList.forEach {
			val matchResult = regex.find(it.trim())
			val id = matchResult!!.destructured.component1()
			val x = matchResult!!.destructured.component2().toInt()
			val y = matchResult!!.destructured.component3().toInt()
			val w = matchResult!!.destructured.component4().toInt()
			val h = matchResult!!.destructured.component5().toInt()
			res = id
			sheet?.let {
				for (ay in y until (y+h) ) {
					var array = arrayOf<Int>()
					for (ax in x until (x+w)) {
						if (sheet[ax][ay] > 1)
							res = ""
					}
				}
			}
			if (!res.isEmpty())
				return res
		}
		return res
	}

	override fun show() {
		println("3 December:")

		val bufferedReader: BufferedReader = File("input/Day3.txt").bufferedReader()
		val lineList = mutableListOf<String>()
		bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }

		subQuestion1(mutableListOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")) `should equal` (4)
		val s1 = System.currentTimeMillis()
		print("Answer part 1: " + subQuestion1(lineList))
		println("\t" + (System.currentTimeMillis() - s1) + " ms")

		subQuestion2(mutableListOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")) `should equal` ("3")
		val s2 = System.currentTimeMillis()
		print("Answer part 2: " + subQuestion2(lineList))
		println("\t" + (System.currentTimeMillis() - s2) + " ms")
		println()

	}
}