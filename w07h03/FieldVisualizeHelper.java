package pgdp.sim;

public class FieldVisualizeHelper {
    private static final String[] emojiSymbols = {"🌱", "🐹", "🐧", "🐺", "🔳"};
    private static final String[] textualSymbols = {"g", "h", "p", "w", "."};

    /**
     * Turns a field description into a Cell-Array.<br>
     * Note that this method does not support emojis as input (they would be too complicated to type manually, anyways).
     * <p>
     * <b>Field Description Language (FDL)</b><br>
     * FDL uses the following syntax:
     * <ul>
     *     <li><code>g</code> = Plant ("Greenery")</li>
     *     <li><code>h</code> = Hamster</li>
     *     <li><code>p</code> = Pingu</li>
     *     <li><code>w</code> = Wolf</li>
     *     <li><code>.</code> = Empty Cell</li>
     * </ul><br>
     * Cells are separated by spaces, lines should, for better readability, be separated by line breaks, but spaces are fine, too.<br>
     * </p><p><b>Example field in FDL</b>
     * <pre><code>
     *     . g g h . h w
     *     w p . . . . .
     *     . . . . . . .
     * </code></pre>
     * Alternatively:
     * <pre><code>
     *     . g g h . h w w p . . . . . . . . . . . .
     * </code></pre></p>
     *
     * @param cellsTextual  A String/text block containing the field description in FDL
     * @param width         The width of the field
     * @param height        The height of the field
     * @return              A Cell-Array with the entities created
     */
    public static Cell[] fieldDescriptionToCellArray(String cellsTextual, int width, int height) {
        cellsTextual = cellsTextual.replace("\n", " ");
        String[] cellFieldTextual = cellsTextual.split(" ");
        Cell[] cellField = new Cell[width * height];

        for (int pos = 0; pos < width*height; pos++) {
            if (textualSymbols[0].equals(cellFieldTextual[pos])){
                cellField[pos] = new Plant();
            } else if (textualSymbols[1].equals(cellFieldTextual[pos])){
                cellField[pos] = new Hamster();
            } else if (textualSymbols[2].equals(cellFieldTextual[pos])){
                cellField[pos] = new Wolf();
            } else if (textualSymbols[3].equals(cellFieldTextual[pos])){
                cellField[pos] = new Pingu();
            } else if (textualSymbols[4].equals(cellFieldTextual[pos])){
                cellField[pos] = null;
            }
        }
        return cellField;
    }

    /**
     * Turns a Cell-Array into a field description.
     * <p>
     * <b>Field Description Language (FDL)</b><br>
     * FDL uses the following syntax:
     * <ul>
     *     <li><code>g/🌱</code> = Plant ("Greenery")</li>
     *     <li><code>h/🐹</code> = Hamster</li>
     *     <li><code>p/🐧</code> = Pingu</li>
     *     <li><code>w/🐺</code> = Wolf</li>
     *     <li><code>./🔳</code> = Empty Cell</li>
     * </ul><br>
     * Cells are separated by spaces, lines will be, for better readability, be separated by line breaks.<br>
     * </p><p><b>Example field in FDL</b>
     * <pre><code>
     *     . g g h . h w
     *     w p . . . . .
     *     . . . . . . .
     * </code></pre>
     * Alternatively:
     * <pre><code>
     *     🔳 🌱 🌱 🐹 🔳 🐹 🐺
     *     🐺 🐧 🔳 🔳 🔳 🔳 🔳
     *     🔳 🔳 🔳 🔳 🔳 🔳 🔳
     * </code></pre></p>
     *
     * @param cellField     A Cell-Array to be turned into an FDL description
     * @param width         The width of the field
     * @param height        The height of the field
     * @param useEmojis     Specifies whether the output should be given as emojis representing the respective cells. Might make it easier to get an overview of the field.
     * @return              A String with the textual description of cellField in FDL
     */
    public static String cellArrayToFieldDescription(Cell[] cellField, int width, int height, boolean useEmojis) {
        String[] charSetToUse = useEmojis ? emojiSymbols : textualSymbols;

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell c = cellField[y * width + x];
                if (c instanceof Plant) {
                    sb.append(charSetToUse[0]);
                } else if (c instanceof Hamster) {
                    sb.append(charSetToUse[1]);
                } else if (c instanceof Wolf) {
                    sb.append(charSetToUse[2]);
                } else if (c instanceof Pingu) {
                    sb.append(charSetToUse[3]);
                } else {
                    sb.append(charSetToUse[4]);
                }

                if (x != width - 1) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
