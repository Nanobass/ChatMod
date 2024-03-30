package nanobass.qol.exception;

import nanobass.qol.command.Command;
import nanobass.qol.command.Command.Parameter;

import java.util.Map;

public class CommandRunException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Command command;
	private Map<String, Parameter> parameters;

	public CommandRunException(Command command, Map<String, Parameter> parameters, Throwable what) {
		super(what);
		this.command = command;
		this.parameters = parameters;
	}

	public Command getCommand() {
		return command;
	}

	public Map<String, Parameter> getParameters() {
		return parameters;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("%c");
		return builder.toString();
	}

}
