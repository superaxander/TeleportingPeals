package alexanders.mods.teleportationpearls

import de.ellpeck.rockbottom.api.RockBottomAPI
import de.ellpeck.rockbottom.api.entity.Entity
import de.ellpeck.rockbottom.api.entity.EntityItem
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer
import de.ellpeck.rockbottom.api.item.ItemInstance
import de.ellpeck.rockbottom.api.render.entity.IEntityRenderer
import de.ellpeck.rockbottom.api.render.entity.ItemEntityRenderer
import de.ellpeck.rockbottom.api.world.IWorld


class PearlEntity(val player: AbstractEntityPlayer, world: IWorld) : EntityItem(world, ItemInstance(RockBottomAPI.ITEM_REGISTRY.get(resource))){
    override fun onGroundHit() {
        player.setPos(this.x, this.y+1)
        this.dead = true
    }
}
