package hu.uni.miskolc.iit.spp.latex;

public enum LatexArgs {
    INPUT("\\input{"),
    TITLE("\\title{"),
    START_ABSTRACT("\\begin{abstract}"),
    END_ABSTRACT("\\end{abstract}"),
    KEYWORDS("\\keywords{"),
    AUTHOR("\\author"),
    HREF("\\href"),
    AFFILIATION("\\affil");

    private String argument;

    private LatexArgs(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}