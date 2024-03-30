package nanobass.qol.command;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import finalforeach.cosmicreach.world.blocks.Block;
import finalforeach.cosmicreach.world.blocks.BlockState;

import java.util.*;

public abstract class Command {

	public static enum ArgumentType {
		Position("[X,Y,Z]"), JSON("Json"), BlockState("BlockState"), Block("Block"), Number("Number"), Boolean("Boolean"), String("String");

		public final String rep;

		private ArgumentType(String rep) {
			this.rep = rep;
		}

	}

	public static class Argument {
		public final String id;
		public final int position;
		public final List<ArgumentType> types;
		public final boolean required;

		public Argument(String id, int position, boolean required, ArgumentType... types) {
			this.id = id;
			this.position = position;
			this.types = new ArrayList<>(Arrays.asList(types));
			Collections.sort(this.types);
			System.out.println(this.types);
			this.required = required;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (required) {
				builder.append("<");
			} else {
				builder.append("[");
			}
			builder.append(id);
			builder.append(": ");
			if (types.size() == 1) {
				builder.append(types.get(0).rep);
			} else {
				builder.append("(");
				for (ArgumentType type : types) {
					builder.append(type.rep);
					builder.append("|");
				}
				builder.deleteCharAt(builder.length() - 1);
				builder.append(")");
			}
			if (required) {
				builder.append(">");
			} else {
				builder.append("]");
			}
			return builder.toString();
		}
	}

	public static class Parameter {
		public final Argument argument;
		public final ArgumentType type;
		public final Object value;

		public Parameter(Argument argument, ArgumentType type, Object value) {
			this.argument = argument;
			this.type = type;
			this.value = value;
		}

		public Vector3 getAsPosition() {
			return (Vector3) value;
		}

		public String getAsString() {
			return (String) value;
		}

		public Number getAsNumber() {
			return (Number) value;
		}

		public Integer getAsInteger() {
			return getAsNumber().intValue();
		}

		public Float getAsFloat() {
			return getAsNumber().floatValue();
		}

		public Boolean getAsBoolean() {
			return (Boolean) value;
		}

		public JsonValue getAsJson() {
			return (JsonValue) value;
		}

		public Block getAsBlock() {
			return (Block) value;
		}

		public BlockState getAsBlockState() {
			return (BlockState) value;
		}

		@Override
		public String toString() {
			return String.format("{%s,%s}", argument.id, value);
		}

	}

	public final String id;
	public final List<Argument> arguments;
	public final String description;

	public Command(String id, String description, Argument... arguments) {
		super();
		this.id = id;
		this.description = description;
		this.arguments = Arrays.asList(arguments);
		this.arguments.sort(new Comparator<Argument>() {
			@Override
			public int compare(Argument o1, Argument o2) {
				return Integer.compare(o1.position, o2.position);
			}
		});
	}

	public abstract void execute(CommandProvider provider, Map<String, Parameter> parameters);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("/").append(id).append(" ");
		for (Argument arg : arguments) {
			builder.append(arg).append(" ");
		}
		return builder.toString();
	}

}
