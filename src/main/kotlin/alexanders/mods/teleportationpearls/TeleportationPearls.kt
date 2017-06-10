package alexanders.mods.teleportationpearls

import de.ellpeck.rockbottom.api.IApiHandler
import de.ellpeck.rockbottom.api.IGameInstance
import de.ellpeck.rockbottom.api.RockBottomAPI
import de.ellpeck.rockbottom.api.assets.IAssetManager
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer
import de.ellpeck.rockbottom.api.event.EventResult
import de.ellpeck.rockbottom.api.event.IEventHandler
import de.ellpeck.rockbottom.api.event.IEventListener
import de.ellpeck.rockbottom.api.event.impl.EntityTickEvent
import de.ellpeck.rockbottom.api.mod.IMod
import de.ellpeck.rockbottom.api.util.reg.IResourceName
import org.newdawn.slick.geom.Vector2f

var resourcePearl: IResourceName? = null
var resourceBouncyPearl: IResourceName? = null
var instance: TeleportationPearls? = null

class TeleportationPearls : IMod {
    override fun getVersion(): String = "0.1"

    override fun getId(): String = "teleportationpearls"

    override fun getDisplayName(): String = "Teleportation Pearls"

    override fun getResourceLocation(): String = "/assets/$id"

    override fun getDescription(): String = "A simple mod that adds pearls that teleport you. What else did you expect?"

    override fun init(game: IGameInstance, assetManager: IAssetManager, apiHandler: IApiHandler, eventHandler: IEventHandler) {
        instance = this
        resourcePearl = RockBottomAPI.createRes(this, "pearl")
        resourceBouncyPearl = RockBottomAPI.createRes(this, "bouncy_pearl")
        val pearl = TeleportationPearl(resourcePearl!!)
        val bouncyPearl = BouncyTeleportationPearl(resourceBouncyPearl!!)
        RockBottomAPI.ITEM_REGISTRY.register(resourcePearl, pearl)
        RockBottomAPI.ITEM_REGISTRY.register(resourceBouncyPearl, bouncyPearl)
        RockBottomAPI.ENTITY_REGISTRY.register(resourcePearl, PearlEntity::class.java)
        RockBottomAPI.ENTITY_REGISTRY.register(resourceBouncyPearl, BouncyPearlEntity::class.java)
        RockBottomAPI.getEventHandler().registerListener(EntityTickEvent::class.java, object : IEventListener<EntityTickEvent> {
            var cooldown = 0
            override fun listen(p0: EventResult, p1: EntityTickEvent): EventResult {
                if (p1.entity is AbstractEntityPlayer && cooldown-- <= 0) {
                    val player = p1.entity as AbstractEntityPlayer
                    if (player.inv[player.selectedSlot] != null && player.inv[player.selectedSlot].item is TeleportationPearl) {
                        if (RockBottomAPI.getGame().container.input.isMouseButtonDown(0)) {
                            val entity = if(player.inv[player.selectedSlot].item is BouncyTeleportationPearl) BouncyPearlEntity(player, p1.entity.world) else PearlEntity(player, p1.entity.world)
                            entity.setPos(player.x, player.y)
                            val dir = angle()
                            entity.motionX += dir.x*.5 
                            entity.motionY += dir.y*-.5
                            p1.entity.world.addEntity(entity)
                            cooldown = 80
                            player.inv[player.selectedSlot].removeAmount(1)
                            if (player.inv[player.selectedSlot].amount <= 0)
                                player.inv[player.selectedSlot] = null
                        }
                    }
                }
                return p0
            }
        })
    }
    private fun angle() : Vector2f {
        val game = RockBottomAPI.getGame().container
        val mouseX = game.input.mouseX
        val mouseY = game.input.mouseY
        val radians = Math.atan2(mouseY-(game.height/2.0), mouseX-(game.width/2.0))
        return Vector2f(Math.cos(radians).toFloat(), Math.sin(radians).toFloat())
    }

}
