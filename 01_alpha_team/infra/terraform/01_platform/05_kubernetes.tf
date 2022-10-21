### Kubernetes Cluster ###

# Kubernetes Cluster
resource "azurerm_kubernetes_cluster" "nr1" {
  name                = var.project_kubernetes_cluster_name
  resource_group_name = azurerm_resource_group.nr1.name
  location            = azurerm_resource_group.nr1.location

  dns_prefix         = "${var.project_kubernetes_cluster_name}-${azurerm_resource_group.nr1.name}"
  kubernetes_version = var.kubernetes_version

  node_resource_group = var.project_kubernetes_cluster_nodepool_name

  default_node_pool {
    name    = "system"
    vm_size = "Standard_D2_v2"

    node_labels = {
      nodePoolName = "system"
    }

    enable_auto_scaling = true
    node_count          = 1
    min_count           = 1
    max_count           = 2
  }

  identity {
    type = "SystemAssigned"
  }
}

# Kubernetes Nodepool - General usage
resource "azurerm_kubernetes_cluster_node_pool" "general" {
  name                  = "general"
  kubernetes_cluster_id = azurerm_kubernetes_cluster.nr1.id
  vm_size               = "Standard_D2_v2"

  orchestrator_version = var.kubernetes_version

  node_labels = {
    nodePoolName = "general"
  }

  enable_auto_scaling = true
  node_count          = 2
  min_count           = 2
  max_count           = 3
}

# Kubernetes Nodepool - Storage optimized
resource "azurerm_kubernetes_cluster_node_pool" "storage" {
  name                  = "storage"
  kubernetes_cluster_id = azurerm_kubernetes_cluster.nr1.id
  vm_size               = "Standard_D2_v2"

  orchestrator_version = var.kubernetes_version

  node_labels = {
    nodePoolName = "storage"
  }

  enable_auto_scaling = true
  node_count          = 2
  min_count           = 2
  max_count           = 3
}
