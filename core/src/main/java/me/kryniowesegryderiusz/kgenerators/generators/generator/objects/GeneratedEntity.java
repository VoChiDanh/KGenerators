package me.kryniowesegryderiusz.kgenerators.generators.generator.objects;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.api.interfaces.IGeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.api.objects.AbstractGeneratedObject;
import me.kryniowesegryderiusz.kgenerators.utils.EntityUtils;
import me.kryniowesegryderiusz.kgenerators.utils.ItemUtils;

public class GeneratedEntity extends AbstractGeneratedObject {
	
	@Getter EntityType entityType;

	public GeneratedEntity() {
		super("entity");
	}

	@Override
	public void regenerate(IGeneratorLocation generatorLocation) {
		Location generateLocation = generatorLocation.getGeneratedBlockLocation().clone().add(0.5, 0, 0.5);
		if (!Main.getMultiVersion().getBlocksUtils().isAir(generatorLocation.getGeneratedBlockLocation().getBlock()))
			generateLocation.add(0,1,0);
		generateLocation.setPitch(-90);
		generateLocation.getWorld().spawnEntity(generateLocation, entityType);
		generatorLocation.scheduleGeneratorRegeneration();
	}

	@Override
	public ItemStack getGuiItem() {
		return ItemUtils.parseItemStack(this.entityType.name()+"_SPAWN_EGG", "Generators file: GeneratedEntity", false);
	}

	@Override
	protected String toStringSpecific() {
		if (this.entityType == null)
			return "None";
		return "Entity: " + this.entityType.toString();
	}

	@Override
	protected boolean compareSameType(AbstractGeneratedObject generatedObject) {
		GeneratedEntity ge = (GeneratedEntity) generatedObject;
		return ge.getEntityType() == this.entityType;
	}

	@Override
	protected boolean loadTypeSpecific(Map<?, ?> generatedObjectConfig) {
		if (generatedObjectConfig.containsKey("entity")) {
			this.entityType = EntityUtils.getEntityType((String) generatedObjectConfig.get("entity"), "Generators file: GeneratedEntity");
			if (this.entityType != null)
				return true;
		}
		return false;
	}
}
