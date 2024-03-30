package nanobass.qol;

public class LineWrapping {
	public static String createWrapped(String text, int limit) {
		StringBuilder builder = new StringBuilder();
		int position = 0;
		while (position < text.length()) {
			int start = position;
			int end = Math.min(position + limit, text.length());
			builder.append(text.substring(start, end)).append("\n");
			position += end - start;
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
