# fiap-revenda-documentos

./mvnw clean install
docker build . -t icarodamiani/fiap-revenda-documentos:latest
docker build . -t icarodamiani/fiap-revenda-documentos:latest
helm upgrade --install documentos chart/documentos/. --kubeconfig ~/.kube/config