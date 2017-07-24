package hu.uni.miskolc.iit.spp.latex;

public enum LatexArgs {
    INPUT("\\input{"),
    TITLE("\\title{"),
    START_ABSTRACT("\\begin{abstract}"),
    END_ABSTRACT("\\end{abstract}"),
    START_KEYWORD("\\begin{keyword"),
    END_KEYWORD("\\end{keyword"),
    KEYWORDS("\\keywords{"),
    AUTHOR("\\author"),
    EAD_COMMAND("\\ead"),
    HREF_COMMAND("\\href"),
    AFFILIATION("\\affil"),
    DOC_CLASS("\\documentclass[10pt,a4paper]{letter}"),
    START_DOCUMENT("\\begin{document}"),
    END_DOCUMENT("\\end{document}");

    private String argument;

    private LatexArgs(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}