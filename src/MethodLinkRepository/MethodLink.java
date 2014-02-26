package MethodLinkRepository;

/**
 * Created by HeierMi on 26.02.14.
 */
public class MethodLink {
    public MethodLink() {
    }

    public MethodLink(String guiMethodComplete, String linkMethodComplete) {
        this.guiMethodComplete = guiMethodComplete;
        this.linkMethodComplete = linkMethodComplete;
    }

    private String guiMethodComplete;
    private String linkMethodComplete;

    public String getLinkMethodComplete() {
        return linkMethodComplete;
    }

    public void setLinkMethodComplete(String linkMethodComplete) {
        this.linkMethodComplete = linkMethodComplete;
    }

    public String getGuiMethodComplete() {
        return guiMethodComplete;
    }

    public void setGuiMethodComplete(String guiMethodComplete) {
        this.guiMethodComplete = guiMethodComplete;
    }

    public String getGuiMethodName() {
        return this.getGuiMethodComplete().substring(0, this.getGuiMethodComplete().indexOf("("));
    }

    public String getLinkMethodName() {
        return this.getLinkMethodComplete().substring(0, this.getLinkMethodComplete().indexOf("("));
    }
}
