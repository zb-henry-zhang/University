package unsw.blackout;

import java.util.ArrayList;
// import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapDFS {
    private List<Entity> vertices;

    public MapDFS(List<Entity> vertices) {
        this.vertices = vertices;
    }

    public Set<Entity> dfs(Entity startEntity) {
        List<Entity> visited = new ArrayList<>();
        Set<Entity> visitedSet = new HashSet<>();
        dfsUtil(startEntity, startEntity, visited, visitedSet);
        return visitedSet;
    }

    private void dfsUtil(Entity sourceEntity, Entity entity, List<Entity> visited, Set<Entity> visitedSet) {
        for (Entity neighbor : vertices) {
            if (!visitedSet.contains(neighbor) && entity.inRange(neighbor) && sourceEntity.isSupportedType(neighbor)) {
                visitedSet.add(neighbor);
                visited.add(neighbor);
                if (neighbor instanceof RelaySatellite) {
                    dfsUtil(sourceEntity, neighbor, visited, visitedSet);
                }
            }
        }
    }
}
