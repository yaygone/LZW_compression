import sys

class TrieNode:

	node_count = -1
	index_coll = []
	
	def __init__(self, content):
		self.index = TrieNode.node_count
		self.content = content
		self.children = []
		TrieNode.node_count += 1

	def find_and_add_data(self, searchTerm):
		if len(searchTerm) != 0:
			if len(self.children) != 0:
				for c in self.children:
					if searchTerm[0] == c.content:
						return c.find_and_add_data(searchTerm[1:])
			self.children.append(TrieNode(searchTerm[0]))
		if self.content != "":
			TrieNode.index_coll.append(self.index)
		return searchTerm
		
	def add_base_level_nodes(self, charset):
		for c in charset:
			self.children.append(TrieNode(c))

if len(sys.argv) != 2:
	print("Usage: python LZWencode.py <data to be encoded>")
else:
	data = [c for c in sys.argv[1]]
	mama_node = TrieNode("")
	mama_node.add_base_level_nodes(sorted(list(set(data))))
	while len(data) > 0:
		data = mama_node.find_and_add_data(data)
	print(",".join([str(c.content) + "=" + str(c.index) for c in mama_node.children]) 
		+ " " 
		+ ",".join([str(i) for i in TrieNode.index_coll]))