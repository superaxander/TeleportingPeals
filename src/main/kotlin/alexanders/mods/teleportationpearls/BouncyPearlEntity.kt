package alexanders.mods.teleportationpearls

import de.ellpeck.rockbottom.api.IGameInstance
import de.ellpeck.rockbottom.api.RockBottomAPI
import de.ellpeck.rockbottom.api.entity.EntityItem
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer
import de.ellpeck.rockbottom.api.item.ItemInstance
import de.ellpeck.rockbottom.api.world.IWorld


class BouncyPearlEntity(val player: AbstractEntityPlayer, world: IWorld) : EntityItem(world, ItemInstance(RockBottomAPI.ITEM_REGISTRY.get(resourceBouncyPearl))) {
    var bouncesLeft = 3
    val facing = player.facing.x
    override fun onGroundHit() {
        if (bouncesLeft-- > 0) {
            this.motionY = -(this.motionY * .85f)
            //entity.motionX = .075* * player.facing.x
            //entity.motionY = 0.5f
            this.onGround = false
        } else {
            player.setPos(this.x, this.y + 1.2f)
            this.dead = true
        }
    }

    override fun update(game: IGameInstance) {
        applyMotion()
        
        move(motionX, motionY)
        if (this.collidedVert) {
            this.onGround = true
            onGroundHit()

        }
        if (this.collidedHor)
            if (bouncesLeft-- > 0) {
                this.motionX = -(this.motionX * .85f)
                //entity.motionX = .075* * player.facing.x
                //entity.motionY = 0.5f
            }
    }
    

}
