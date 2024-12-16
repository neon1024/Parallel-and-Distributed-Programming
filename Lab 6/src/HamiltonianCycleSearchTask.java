import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class HamiltonianCycleSearchTask implements Runnable {
    private final List<List<Integer>> graph;
    private final int startingNode;
    private final AtomicBoolean foundHamiltonianCycle;
    private final List<Integer> possiblePath;
    private final List<Integer> output;
    private final Lock lock;
    private final List<Boolean> visited;

    public HamiltonianCycleSearchTask(List<List<Integer>> graph, int startingNode, AtomicBoolean foundHamiltonianCycle, List<Integer> output) {
        this.graph = graph;
        this.startingNode = startingNode;
        this.foundHamiltonianCycle = foundHamiltonianCycle;
        this.possiblePath = new ArrayList<>();
        this.output = output;
        this.lock = new ReentrantLock();
        this.visited = new ArrayList<>();

        for (int i = 0; i < this.graph.size(); i++) {
            this.visited.add(false);
        }
    }

    private void foundCycle(){
        this.foundHamiltonianCycle.set(true);

        this.lock.lock();
        this.possiblePath.add(this.startingNode);
        this.output.clear();
        this.output.addAll(this.possiblePath);
        this.lock.unlock();
    }

    private void goToNode(int nextNode){
        // stop if cycle is already found
        if (foundHamiltonianCycle.get()) {
            return;
        }

        this.possiblePath.add(nextNode);
        this.visited.set(nextNode, true);

        // base case: check if we visited all nodes and can return to the start node
        // we check the current path excluding the final node which is the starting node
        if (this.possiblePath.size() == this.graph.size()) {
            if (this.graph.get(nextNode).contains(this.startingNode)) {
                this.foundCycle();
                return;
            }
        } else {
            // recursive case: explore neighbors
            for (Integer outboundNeighbour : this.graph.get(nextNode)) {
                if (!this.visited.get(outboundNeighbour)) {
                    this.goToNode(outboundNeighbour);

                    // exit early if a cycle is found
                    if (foundHamiltonianCycle.get()) {
                        return;
                    }
                }
            }
        }

        // backtrack: remove the current node and mark it as unvisited
        this.possiblePath.remove(this.possiblePath.size() - 1);
        this.visited.set(nextNode, false);
    }

    @Override
    public void run() {
        this.goToNode(this.startingNode);
    }
}
