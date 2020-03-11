import sys

output_list = []
mama_node = None

class TrieNode:

	node_count = -1
	
	def __init__(self, content = ""):
		self.index = TrieNode.node_count
		self.content = content
		self.children = []
		TrieNode.node_count += 1

	def build_trie(self, index_list):
		if self.index == index_list[0]:
			output_list.append(self)
			next_ind = -1 if len(index_list) == 1 else index_list[1]
			newChild = TrieNode(mama_node.head_symbol(self.index if next_ind == TrieNode.node_count else next_ind))
			self.children.append(newChild)
		else:
			for c in self.children:
				c.build_trie(index_list)

	def head_symbol(self, search_index):
		for c in self.children:
			if c.head_symbol(search_index) != None:
				return c.content if self.index == -1 else self.content
		if self.index == search_index:
			return self.content
		return None

	def expand_content(self, prefix = ""):
		self.content = prefix + self.content
		for c in self.children:
			c.expand_content(self.content)

if len(sys.argv) >= 3:
	print('Usage: python LZWdecode.py <top-level dictionary eg "a=0 b=1"> <data to be decoded>')
	exit
indices = list(map(int, sys.argv[2].split(",")))
mama_node = TrieNode()
for entry in sys.argv[1].split(","):
	mama_node.children.append(TrieNode(entry))
while len(indices) > 0:
	mama_node.build_trie(indices)
	indices = indices[1:]
mama_node.expand_content()
print("".join([node.content for node in output_list]))