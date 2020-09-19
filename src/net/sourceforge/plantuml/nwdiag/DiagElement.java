/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.nwdiag;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ComponentStyle;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class DiagElement {

	private USymbol shape = USymbol.RECTANGLE;
	private final String name;
	private String description;
	private final Network mainNetwork;
	private final ISkinSimple spriteContainer;
	private boolean hasItsOwnColumn = true;

	@Override
	public String toString() {
		return name;
	}

	public DiagElement(String name, Network network, ISkinSimple spriteContainer) {
		this.description = name;
		this.mainNetwork = network;
		this.name = name;
		this.spriteContainer = spriteContainer;
	}

	private TextBlock toTextBlock(String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 0) {
			return TextBlockUtils.empty(0, 0);
		}
		s = s.replace(", ", "\\n");
		return Display.getWithNewlines(s).create(getFontConfiguration(), HorizontalAlignment.LEFT, spriteContainer);
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	public LinkedElement asTextBlock(Map<Network, String> conns, Network next) {
		final Map<Network, TextBlock> conns2 = new LinkedHashMap<Network, TextBlock>();
		for (Entry<Network, String> ent : conns.entrySet()) {
			conns2.put(ent.getKey(), toTextBlock(ent.getValue()));
		}
		final SymbolContext symbolContext = new SymbolContext(ColorParam.activityBackground.getDefaultValue(),
				ColorParam.activityBorder.getDefaultValue()).withShadow(3);
		final TextBlock desc = toTextBlock(description);
		final TextBlock box = shape.asSmall(TextBlockUtils.empty(0, 0), desc, TextBlockUtils.empty(0, 0), symbolContext,
				HorizontalAlignment.CENTER);
		return new LinkedElement(this, box, mainNetwork, next, conns2);
	}

	public String getDescription() {
		return description;
	}

	public final Network getMainNetwork() {
		return mainNetwork;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public final void setShape(String shapeName) {
		final USymbol shapeFromString = USymbol.fromString(shapeName, ActorStyle.STICKMAN, ComponentStyle.RECTANGLE,
				PackageStyle.RECTANGLE);
		if (shapeFromString != null) {
			this.shape = shapeFromString;
		}
	}

	public void doNotHaveItsOwnColumn() {
		this.hasItsOwnColumn = false;
	}

	public final boolean hasItsOwnColumn() {
		return hasItsOwnColumn;
	}

}
