// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.attributeset;


public class DeleteAttributeSetDto extends AbstractAttributeSetCommandDto implements AttributeSetCommand.DeleteAttributeSet {

    public DeleteAttributeSetDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

    public AttributeSetCommand.DeleteAttributeSet toDeleteAttributeSet()
    {
        AbstractAttributeSetCommand.SimpleDeleteAttributeSet command = new AbstractAttributeSetCommand.SimpleDeleteAttributeSet();
        ((AbstractAttributeSetCommandDto)this).copyTo(command);
        return command;
    }
}

