// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.article;


public class DeleteArticleDto extends AbstractArticleCommandDto implements ArticleCommand.DeleteArticle {

    public DeleteArticleDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public ArticleCommand.DeleteArticle toDeleteArticle()
    {
        AbstractArticleCommand.SimpleDeleteArticle command = new AbstractArticleCommand.SimpleDeleteArticle();
        ((AbstractArticleCommandDto)this).copyTo(command);
        return command;
    }
}
