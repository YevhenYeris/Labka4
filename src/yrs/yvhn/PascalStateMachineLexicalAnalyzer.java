package yrs.yvhn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PascalStateMachineLexicalAnalyzer {

    private String _code = "";
    private String _resOfAnalysis = "";

    public PascalStateMachineLexicalAnalyzer(String fileName) throws IOException
    {
        _code = Files.readString(Paths.get(fileName));
    }

    public boolean isNumber(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("numberSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isCharConst(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("charconstSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isDirective(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("directiveSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isBracket(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("bracketSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isKeyWord(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("keywordSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isOperator(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("operatorSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isDelimiter(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("delimiterSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isDelimiterLexeme(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("delimiterlexemeSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public boolean isIdentifier(String text) throws IOException
    {
        FiniteStateMachine stateMachine = new FiniteStateMachine("identifierSM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
        //return text.matches(IdentifierRegex);
    }

    public boolean isEmpty(String text) throws IOException {

        FiniteStateMachine stateMachine = new FiniteStateMachine("emptySM.txt");
        stateMachine.ComputeResult(text);
        return stateMachine.IsFinished();
    }

    public String ReadTokens() throws IOException
    {
            int left = 0, right = 0;
            int len = _code.length();

            while (right < len && left <= right) {

                String currChar = Character.toString(_code.charAt(right));

                if (currChar.equals("{")) {
                    while (right <= len && left <= right && _code.charAt(right) != '}')
                    {
                        ++right;
                    }
                    _resOfAnalysis += "{} - comment\n";
                    left = ++right;
                    continue;
                }
                if (currChar.equals("/") && ++right < len && _code.charAt(right) == '/') {
                    while (right < len && left <= right && _code.charAt(right) != '\n')
                    {
                        ++right;
                    }
                    _resOfAnalysis += "// - comment\n";
                    left = ++right;
                    continue;
                }
                if (currChar.equals("(") && right + 1 < len && _code.charAt(right + 1) == '*') {
                    ++right;
                    while (++right < len && left <= right && _code.charAt(right) != '*' && right + 1 < len && _code.charAt(right + 1) != ')')
                    {
                    }
                    _resOfAnalysis += "(**) - comment\n";
                    right += 2;
                    left = right;
                    continue;
                }

                if (currChar.equals(":") && right + 1 < len && _code.charAt(right + 1) == '=')
                {
                    _resOfAnalysis += ":= - operator\n";
                    right += 2;
                    left = right;
                    continue;
                }

                if (currChar.equals("'")) {
                    while (++right <= len && left <= right && _code.charAt(right) != '\'')
                    {

                    }
                    _resOfAnalysis += _code.substring(left, ++right) + " - character constant\n";
                    left = right;
                    continue;
                }

                if (!isDelimiter(currChar))
                    right++;

                if (isDelimiter(currChar) && left == right) {
                    if (isOperator(currChar))
                        _resOfAnalysis += currChar + " - " + "operator\n";

                    if (isBracket(currChar))
                        _resOfAnalysis += currChar + " - bracket\n";

                    if (isDelimiterLexeme(currChar))
                        _resOfAnalysis += currChar + " - delimiter\n";

                    right++;
                    left = right; 
                } else if (isDelimiter(currChar) && left != right
                        || right == len) {

                    String substr = _code.substring(left, right);

                    if (isKeyWord(substr))
                        _resOfAnalysis += substr + " - " + "keyword\n";

                    else if (isDirective(substr))
                        _resOfAnalysis += substr + " - " + "preprocessor directive\n";

                    else if (isOperator(substr))
                        _resOfAnalysis += substr + " - " + "operator\n";

                    else if (isNumber(substr))
                        _resOfAnalysis += substr + " - " + "number\n";

                    else if (isIdentifier(substr))
                        _resOfAnalysis += substr + " - " + "identifier\n";

                    else if (substr.equals("end."))
                        _resOfAnalysis += "end - keyword\n. - delimiter\n";

                    else if (!isEmpty(substr))
                        _resOfAnalysis += substr + " - " + "undefined\n";

                    left = right;
                }
            }
            return _resOfAnalysis;
    }
}
