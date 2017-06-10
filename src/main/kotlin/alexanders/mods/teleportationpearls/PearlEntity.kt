package alexanders.mods.teleportationpearls

import de.ellpeck.rockbottom.api.RockBottomAPI
import de.ellpeck.rockbottom.api.entity.EntityItem
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer
import de.ellpeck.rockbottom.api.item.ItemInstance
import de.ellpeck.rockbottom.api.world.IWorld


class PearlEntity(val player: AbstractEntityPlayer, world: IWorld) : EntityItem(world, ItemInstance(RockBottomAPI.ITEM_REGISTRY.get(resourcePearl))){
    override fun onGroundHit() {
        player.setPos(this.x, this.y+1.2f)
        this.dead = true
    }
    
}
