import java.io.BufferedReader
import java.io.File
import kotlin.test.assertEquals

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