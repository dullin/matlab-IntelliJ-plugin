package com.github.korniloval.matlab.highlighting

import com.intellij.codeInsight.highlighting.BraceMatchingUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import junit.framework.TestCase
import java.io.File

private const val PAIR_MARKER = "<pair>"

class ParenthHighlightingTest : LightPlatformCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String = "src/test/resources/highlighting"

    fun testParenth() = doTest()
    fun testBraces() = doTest()
    fun testBrackets() = doTest()

    private fun doTest() {
        val testFile = File(testDataPath, getTestName(true) + ".m")

        myFixture.configureByText(testFile.name, testFile.readText())

        val pairOffset = myFixture.file.text.indexOf(PAIR_MARKER)

        val editor = myFixture.editor
        val document = editor.document
        WriteCommandAction.runWriteCommandAction(null) { document.replaceString(pairOffset, pairOffset + PAIR_MARKER.length, "") }

        PsiDocumentManager.getInstance(project).commitDocument(document)

        val actual = BraceMatchingUtil.getMatchedBraceOffset(
                editor,
                editor.caretModel.offset < pairOffset,
                myFixture.file
        )

        TestCase.assertEquals(pairOffset, actual)
    }
}